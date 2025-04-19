package com.example.generationutils.extensions

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import kotlin.reflect.KClass

fun Resolver.findAnnotations(
    kClass: KClass<*>,
) = getSymbolsWithAnnotation(kClass.qualifiedName.toString())
    .filterIsInstance<KSClassDeclaration>()
    .toList()

val KSPropertyDeclaration.isTypeNullable: Boolean
    get() = type.resolve().isMarkedNullable

val KSClassDeclaration.isSealedClass: Boolean
    get() = getSealedSubclasses().toList().isNotEmpty()

val KSClassDeclaration.isEnum: Boolean
    get() = classKind == ClassKind.ENUM_CLASS

val KSPropertyDeclaration.typeName
    get() = type.resolve().declaration.simpleName.asString()

val KSType.name
    get() = declaration.simpleName.asString()

val KSPropertyDeclaration.typeQualifiedName
    get() = type.resolve().declaration.qualifiedName?.asString()

fun KSAnnotated.getAnnotation(kClass: KClass<*>): KSAnnotation? {
    return annotations.firstOrNull {
        isSame(kClass, it)
    }
}

fun isSame(kClass: KClass<*>, declaration: KSDeclaration): Boolean {
    return isSame(kClass, declaration.simpleName)
}

fun isSame(kClass: KClass<*>, annotation: KSAnnotation): Boolean {
    return isSame(kClass, annotation.shortName)
}

fun isSame(kClass: KClass<*>, name: KSName): Boolean {
    return kClass.simpleName == name.getShortName()
}

@Suppress("UNCHECKED_CAST")
fun <T> KSAnnotation.getArgument(index: Int): T {
    return arguments[index].value as T
}

inline fun <reified T> Sequence<T>.toTypedArray(): Array<T> {
    return this.toList().toTypedArray()
}

/**
 * Iterates through a collection until the result of the provided function is not null
 * @return a non-null result of the provided function, or null if it's always null
 */
fun <T, R> Collection<T>.whereNotNullOrNull(function: (T) -> R?): R? {
    forEach {
        function(it)?.let { value ->
            return value
        }
    }
    return null
}

/**
 * It must exist because of the names of sealed classes
 */
val ClassName.compositedName: String
    get() = simpleNames.joinToString(".")

/**
 * It must exist because of the names of sealed classes
 */
val ClassName.compositedSimpleName: String
    get() = simpleNames.joinToString("")

/**
 * @param list is a list of object we are iterating through
 * @param iteration the action we are doing in the current iteration,
 * the receiver is the result of the previous iteration
 * @return the result of the last iteration
 */
fun <T, R> T.iterateOn(list: List<R>, iteration: T.(R) -> T): T {
    var currentIteration = this
    list.forEach { currentIteration = currentIteration.iteration(it) }
    return currentIteration
}

inline fun <reified T> parameterized(
    vararg typeArguments: TypeName,
): ParameterizedTypeName {
    return T::class.asClassName().parameterizedBy(*typeArguments)
}
