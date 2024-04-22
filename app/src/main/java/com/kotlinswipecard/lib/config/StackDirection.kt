package com.kotlinswipecard.lib.config

/**
 * Defines the possible directions a stack operation can manipulate elements.
 * This enum is used to specify the direction in which elements are added or removed in a stack-like structure.
 */
enum class StackDirection {
    /**
     * Represents a leftward direction, indicating that elements should be added or removed from the left side.
     * Left returns 4 as a value.
     */
    Left,

    /**
     * Represents an upward direction, indicating that elements should be added or removed from the top.
     * Up returns 1 as a value.
     */
    Up,

    /**
     * Represents a rightward direction, indicating that elements should be added or removed from the right side.
     * Right returns 8 as a value.
     */
    Right,

    /**
     * Represents a downward direction, indicating that elements should be added or removed from the bottom.
     * Down returns 2 as a value.
     */
    Down,
}
