//
// MIT License
//
// Copyright (c) 2024 Incendo
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package org.incendo.cloud.translations;

import java.util.Locale;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionProvider;

/**
 * Caption provider that provides translated captions.
 *
 * @param <C> command sender type
 */
public interface TranslatedCaptionProvider<C> extends CaptionProvider<C> {

    /**
     * Returns an empty translation caption provider that always returns {@code null}.
     *
     * @param <C> command sender type
     * @return the translation
     */
    @SuppressWarnings("unchecked")
    static <C> @NonNull TranslatedCaptionProvider<C> empty() {
        return (TranslatedCaptionProvider<C>) Empty.EMPTY;
    }

    /**
     * Returns whether the provider is empty.
     *
     * @return {@code true} if the translation is empty, {@code false} if not
     */
    boolean isEmpty();

    /**
     * Returns the provider locale.
     *
     * @return the locale
     */
    @NonNull Locale locale();

    final class Empty<C> implements TranslatedCaptionProvider<C> {

        private static final Empty<?> EMPTY = new Empty<>();

        private Empty() {
        }

        @Override
        public @Nullable String provide(@NonNull final Caption caption, @NonNull final C recipient) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public @NonNull Locale locale() {
            return Locale.getDefault();
        }
    }
}
