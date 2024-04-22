package com.kotlinswipecard.lib.config

/**
 * A data class representing the configuration of a swiper component.
 * This class provides various properties to customize the behavior and appearance of the swiper.
 *
 * @property showCount The number of items to show simultaneously in the swiper view. Default is 1.
 * @property itemScale The scale factor for the items in the swiper. Default is 0f, meaning no scaling.
 *                A value of 1f would mean no change, less than 1f would reduce the size, and more than 1f would increase the size.
 * @property itemTranslate The translation offset for items, affecting their positioning along the X or Y axis depending on stack direction.
 *                Default is 0f, which means no translation.
 * @property itemRotation The rotation angle of the items in degrees. Default is 0f, which means no rotation.
 * @property swipeDirections Defines the allowed swipe directions. It utilizes the `SwipeDirection` annotation for type safety.
 *                Default is `SwipeDirection.ALL`, allowing swipes in all directions.
 * @property stackDirection The direction in which items are stacked in the swiper.
 *                The `StackDirection` enum specifies the stack direction, with `StackDirection.Down` as the default.
 */
data class SwiperConfig(
    val showCount: Int = 1,
    val itemScale: Float = 0f,
    val itemTranslate: Float = 0f,
    val itemRotation: Float = 0f,
    @SwipeDirection
    val swipeDirections: Int = SwipeDirection.ALL,
    val stackDirection: StackDirection = StackDirection.Down
)