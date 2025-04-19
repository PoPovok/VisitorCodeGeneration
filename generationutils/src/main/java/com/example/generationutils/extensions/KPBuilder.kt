package com.example.generationutils.extensions

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo

fun FileSpec.Builder.writeToKsp(codeGenerator: CodeGenerator) {
    build().writeTo(codeGenerator, Dependencies.ALL_FILES)
}

inline fun buildFunction(name: String, block: FunSpec.Builder.() -> FunSpec.Builder): FunSpec {
    return FunSpec.builder(name).block().build()
}

fun FunSpec.Builder.addCode(block: CodeBlock.Builder.() -> CodeBlock.Builder): FunSpec.Builder {
    return addCode(
        buildCodeBlock {
            block()
        }
    )
}

fun FileSpec.Builder.addClass(name: String, block: TypeSpec.Builder.() -> TypeSpec.Builder): FileSpec.Builder {
    return addType(
        TypeSpec.classBuilder(name).block().build()
    )
}

fun FileSpec.Builder.addObject(name: String, block: TypeSpec.Builder.() -> TypeSpec.Builder): FileSpec.Builder {
    return addType(
        TypeSpec.objectBuilder(name).block().build()
    )
}

fun FileSpec.Builder.addFunction(name: String, block: FunSpec.Builder.() -> FunSpec.Builder): FileSpec.Builder {
    return addFunction(
        buildFunction(name) {
            block()
        }
    )
}

fun TypeSpec.Builder.addFunction(name: String, block: FunSpec.Builder.() -> FunSpec.Builder): TypeSpec.Builder {
    return addFunction(
        buildFunction(name) {
            block()
        }
    )
}

fun CodeBlock.Builder.withControlFlow(
    controlFlow: String,
    block: CodeBlock.Builder.() -> CodeBlock.Builder
): CodeBlock.Builder {
    return this
        .beginControlFlow(controlFlow)
        .indent()
        .block()
        .unindent()
        .endControlFlow()
}

fun CodeBlock.Builder.withInlineControlFlow(
    controlFlow: String,
    block: CodeBlock.Builder.() -> CodeBlock.Builder
): CodeBlock.Builder {
    return this
        .beginControlFlow(controlFlow)
        .indent()
        .block()
        .unindent()
        .unindent()
        .add("}")
}

fun CodeBlock.Builder.withInvocation(
    invocation: String,
    block: CodeBlock.Builder.() -> CodeBlock.Builder
): CodeBlock.Builder {
    return this
        .add("$invocation(\n")
        .withIndent {
            block()
        }
        .add(")\n")
}

fun CodeBlock.Builder.withInlineInvocation(
    invocation: String,
    block: CodeBlock.Builder.() -> CodeBlock.Builder
): CodeBlock.Builder {
    return this
        .add("$invocation(\n")
        .withIndent {
            block()
        }
        .add(")")
}

fun CodeBlock.Builder.withIndent(block: CodeBlock.Builder.() -> CodeBlock.Builder): CodeBlock.Builder {
    return this
        .indent()
        .indent()
        .block()
        .unindent()
        .unindent()
}
