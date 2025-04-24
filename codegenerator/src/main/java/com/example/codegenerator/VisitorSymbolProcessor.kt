package com.example.codegenerator

import com.example.codegenerator.annotation.Visitor
import com.example.generationutils.extensions.findAnnotations
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class VisitorSymbolProcessor(val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        throw Exception("The code generator is working")

        val visitorDeclarations = resolver.findAnnotations(Visitor::class)
        if (visitorDeclarations.isEmpty()) return emptyList()

        return emptyList()
    }
}