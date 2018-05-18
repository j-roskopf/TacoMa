package com.example.joeroskopf.resume.android.extensions
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.support.annotation.ColorInt
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan


/**
 * NOTE - Borrowed from AndroidKTX https://github.com/android/android-ktx/blob/master/src/main/java/androidx/core/text/SpannableStringBuilder.kt
 * I could not get the AndroidKTX library to compile successfully at this time with androidx
 */
/**
 * Wrap appended text in [builderAction] in [spans].
 *
 * Note: the spans will only have the correct position if the [builderAction] only appends or
 * replaces text. Inserting, deleting, or clearing the text will cause the span to be placed at
 * an incorrect position.
 */
inline fun SpannableStringBuilder.inSpans(
        vararg spans: Any,
        builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    for (span in spans) setSpan(span, start, length, SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * Wrap appended text in [builderAction] in [span].
 *
 * Note: the span will only have the correct position if the `builderAction` only appends or
 * replaces text. Inserting, deleting, or clearing the text will cause the span to be placed at
 * an incorrect position.
 */
inline fun SpannableStringBuilder.inSpans(
        span: Any,
        builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    setSpan(span, start, length, SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * Wrap appended text in [builderAction] in a bold [StyleSpan].
 *
 * @see SpannableStringBuilder.inSpans
 */
inline fun SpannableStringBuilder.bold(builderAction: SpannableStringBuilder.() -> Unit) =
        inSpans(StyleSpan(BOLD), builderAction = builderAction)

/**
 * Wrap appended text in [builderAction] in an italic [StyleSpan].
 *
 * @see SpannableStringBuilder.inSpans
 */
inline fun SpannableStringBuilder.italic(builderAction: SpannableStringBuilder.() -> Unit) =
        inSpans(StyleSpan(ITALIC), builderAction = builderAction)

/**
 * Wrap appended text in [builderAction] in a [ForegroundColorSpan].
 *
 * @see SpannableStringBuilder.inSpans
 */
inline fun SpannableStringBuilder.color(
        @ColorInt color: Int,
        builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(ForegroundColorSpan(color), builderAction = builderAction)
