package com.example.codegenerator

import com.example.codegenerator.annotation.Visitor
import com.example.codegenerator.annotation.VisitorFor
import com.example.generationutils.extensions.addClass
import com.example.generationutils.extensions.addFunction
import com.example.generationutils.extensions.addObject
import com.example.generationutils.extensions.findAnnotations
import com.example.generationutils.extensions.getAnnotation
import com.example.generationutils.extensions.getArgument
import com.example.generationutils.extensions.isSealedClass
import com.example.generationutils.extensions.iterateOn
import com.example.generationutils.extensions.writeToKsp
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class VisitorSymbolProcessor(val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val visitorDeclarations = resolver.findAnnotations(Visitor::class)
        if (visitorDeclarations.isEmpty()) return emptyList()

        val visitorDeclaration = visitorDeclarations[0]

        val themeDeclaration = visitorDeclaration // Visitor
            .getAnnotation(Visitor::class)!! // Theme, ......
            .getArgument<KSType>(0) // Theme
            .declaration as KSClassDeclaration

        val sealedSubclasses = if (themeDeclaration.isSealedClass) {
            themeDeclaration.getSealedSubclasses().toList()
        } else {
            throw Exception("it needs to be a sealed class")
        }

        FileSpec
            .builder("com.example.codegenerator", "ThemeVisitorBase")
            .addClass("ThemeVisitorBase") {
                this
                    .addModifiers(KModifier.ABSTRACT)
                    .iterateOn(sealedSubclasses) { sealedSubclass ->
                        val name = sealedSubclass.simpleName.asString()

                        addFunction("getTheme") {
                            this.addModifiers(KModifier.ABSTRACT)
                            returns(ClassName("androidx.compose.ui.graphics", "Color"))
                                .addParameter(ParameterSpec(name.lowercase(), sealedSubclass.toClassName()))
                        }
                    }
            }.writeToKsp(environment.codeGenerator)

        val visitorInstanceDeclarations = resolver.findAnnotations(VisitorFor::class)
        if (visitorInstanceDeclarations.isEmpty()) return emptyList()

        val visitorInstances = visitorInstanceDeclarations
            .map { it.simpleName.asString() + "()" }
            .joinToString(", ")

        FileSpec
            .builder("com.example.codegenerator", "ThemeVisitorHolder")
            .iterateOn(visitorInstanceDeclarations){ visitorInstanceDeclaration ->
                addImport(
                    visitorInstanceDeclaration.packageName.asString(),
                    visitorInstanceDeclaration.simpleName.asString()
                )
            }
            .addObject("ThemeVisitorHolder") {
                this.addProperty(
                    PropertySpec.builder(
                        "visitor",
                        List::class.asClassName().parameterizedBy(
                            ClassName("com.example.codegenerator", "ThemeVisitorBase")
                        )
                    ).initializer("listOf($visitorInstances)")
                        .build()
                )
            }.writeToKsp(environment.codeGenerator)


        return emptyList()
    }
}