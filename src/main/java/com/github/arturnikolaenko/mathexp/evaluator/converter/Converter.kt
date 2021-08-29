package com.github.arturnikolaenko.mathexp.evaluator.converter

import com.github.arturnikolaenko.mathexp.evaluator.converter.UnitCategory.LENGTH
import java.util.logging.Logger

object Converter {

    private val logger = Logger.getLogger(Converter::class.java.name)

    private val categoryTable = mapOf(
        "m" to LENGTH,
        "km" to LENGTH,
        "cm" to LENGTH,
        "mm" to LENGTH,
        "ft" to LENGTH,
        "in" to LENGTH,
        "yd" to LENGTH,
        "mile" to LENGTH
    )

    private val coefTable = mapOf(
        "m" to mapOf(
            "m" to 1.0,
            "km" to .001,
            "cm" to 100.0,
            "mm" to 1000.0,
            "ft" to 3.28083,
            "in" to 39.3701,
            "yd" to 1.093613,
            "mile" to 6.2137e-4
        ),

        "km" to mapOf(
            "m" to 1000.0,
            "km" to 1.0,
            "cm" to 1e5,
            "mm" to 1e6,
            "ft" to 3280.83,
            "in" to 39370.1,
            "yd" to 1093.613,
            "mile" to 1.6093
        ),

        "cm" to mapOf(
            "m" to 0.01,
            "cm" to 1.0,
            "km" to 1e-5,
            "mm" to 10.0,
            "ft" to 0.0328084,
            "in" to 0.393701,
            "yd" to 0.0109361,
            "mile" to 6.2137e-6
        ),

        "mm" to mapOf(
            "m" to 0.001,
            "cm" to 0.1,
            "km" to 1e-6,
            "mm" to 1.0,
            "ft" to 0.00328084,
            "in" to 0.0393701,
            "yd" to 0.00109361,
            "mile" to 6.2137e-7
        ),

        "ft" to mapOf(
            "m" to 0.3048,
            "cm" to 30.48,
            "km" to 0.0003048,
            "mm" to 304.8,
            "ft" to 1.0,
            "in" to 12.0,
            "yd" to 0.333333,
            "mile" to 0.000189394
        ),

        "in" to mapOf(
            "m" to 0.0254,
            "cm" to 2.54,
            "km" to 2.54e-5,
            "mm" to 25.4,
            "ft" to 0.0833333,
            "in" to 1.0,
            "yd" to 0.0277778,
            "mile" to 1.5783e-5
        ),

        "yd" to mapOf(
            "m" to 0.9144,
            "cm" to 91.44,
            "km" to 0.0009144,
            "mm" to 914.4,
            "ft" to 3.0,
            "in" to 36.0,
            "yd" to 1.0,
            "mile" to 0.000568182
        ),

        "mile" to mapOf(
            "m" to 1609.34,
            "cm" to 160934.0,
            "km" to 1.60934,
            "mm" to 1.609e+6,
            "ft" to 5280.0,
            "in" to 63360.0,
            "yd" to 1760.0,
            "mile" to 1.0
        )
    )

    fun convert(value: Double, from: String, to: String, silent: Boolean = true): Double {
        val cf: Double? = coefTable[from]?.get(to)

        return if (cf == null) {
            val msg = "Incompatible units: '$from' '$to'"

            if (silent) {
                logger.warning(msg)
            } else {
                throw IncompatibleConversionException(msg)
            }

            value
        } else {
            value * cf
        }
    }

    fun getCategory(unit: String): UnitCategory? {
        return categoryTable[unit]
    }

    fun isSupportedUnit(unit: String): Boolean {
        return unit in categoryTable.keys
    }
}