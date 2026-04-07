package com.libowen.fakecall.domain.usecase

import com.libowen.fakecall.domain.model.CallerInfo
import javax.inject.Inject

class GenerateRandomCallerUseCase @Inject constructor() {

    private val relationships = listOf(
        "Mom",
        "Dad",
        "Partner",
        "Boss",
        "Sister",
        "Brother",
        "Grandma",
        "Coworker Jake",
        "Coworker Sarah",
        "Dentist",
        "Doctor"
    )

    private val maleNames = listOf(
        "James", "William", "Oliver", "Jack", "Harry", "George",
        "Noah", "Charlie", "Jacob", "Ethan", "Mason", "Logan",
        "Lucas", "Liam", "Aiden", "Ryan", "Nathan", "Tyler",
        "Aaron", "Dylan", "Brandon", "Connor", "Evan", "Ian"
    )

    private val femaleNames = listOf(
        "Emma", "Olivia", "Ava", "Sophia", "Isabella", "Mia",
        "Charlotte", "Amelia", "Harper", "Emily", "Abigail", "Madison",
        "Elizabeth", "Sofia", "Avery", "Ella", "Scarlett", "Grace",
        "Chloe", "Victoria", "Lily", "Hannah", "Zoe", "Natalie"
    )

    private val surnames = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones",
        "Garcia", "Miller", "Davis", "Wilson", "Taylor",
        "Anderson", "Thomas", "Jackson", "White", "Harris",
        "Martin", "Thompson", "Young", "Moore", "Clark"
    )

    private val mobilePrefixes = listOf(
        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
        "30", "31", "40", "41", "42", "50", "51", "52", "53",
        "60", "61", "71", "81", "91", "93"
    )

    operator fun invoke(): CallerInfo {
        // 60% relationship label, 40% full name
        val name = if (Math.random() < 0.6) {
            relationships.random()
        } else {
            val isMale = Math.random() < 0.5
            val firstName = if (isMale) maleNames.random() else femaleNames.random()
            "$firstName ${surnames.random()}"
        }

        return CallerInfo(
            id = "random",
            name = name,
            number = generateNumber(),
            avatarUri = null
        )
    }

    private fun generateNumber(): String {
        val prefix = mobilePrefixes.random()
        val suffix = (100000..999999).random()
        val raw = "$prefix$suffix"
        return "${raw.substring(0, 2)} ${raw.substring(2, 4)} ${raw.substring(4, 6)} ${raw.substring(6, 8)}"
    }
}
