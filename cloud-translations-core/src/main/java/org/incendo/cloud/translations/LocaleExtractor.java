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

import io.leangen.geantyref.TypeToken;
import java.util.Locale;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Extractor that extracts {@link Locale locales} from command senders.
 *
 * @param <C> command sender type
 */
@API(status = API.Status.STABLE)
public interface LocaleExtractor<C> {

    /**
     * Extracts the locale from the given {@code recipient}.
     *
     * @param recipient recipient to extract locale from
     * @return the extracted locale
     */
    @NonNull Locale extract(@NonNull C recipient);

    /**
     * Returns a new {@link Builder}.
     *
     * @param <C> command sender type
     * @return new builder
     */
    static <C> Builder<C> builder() {
        return new LocaleExtractorBuilderImpl<>();
    }

    /**
     * Builder for a {@link LocaleExtractor} that delegates to functions based on
     * command sender type.
     *
     * @param <C> command sender type
     * @see #senderType(TypeToken, Function)
     * @see #fallback(LocaleExtractor)
     */
    interface Builder<C> {

        /**
         * Sets the fallback {@link LocaleExtractor} for when none of the configured {@link #senderType(TypeToken, Function)}
         * extractors match the sender. Defaults to {@code LocaleExtractor<C> default = recipient -> Locale.getDefault()}.
         *
         * @param fallback fallback extractor
         * @return this builder
         */
        @NonNull Builder<C> fallback(@NonNull LocaleExtractor<C> fallback);

        /**
         * Sets {@link #fallback(LocaleExtractor)} with a constant locale.
         *
         * @param fallback fallback locale
         * @return this builder
         */
        default @NonNull Builder<C> fallback(final @NonNull Locale fallback) {
            return this.fallback($ -> fallback);
        }

        /**
         * Registers a locale extractor for a specific sender type to this builder.
         *
         * <p>Extractors are tried in LIFO registration order. Extractors may return {@code null}
         * to pass the recipient to the next extractor, even when the type matches.</p>
         *
         * @param senderType specific command sender type
         * @param extractor  locale extractor
         * @param <S>        specific command sender type
         * @return this builder
         */
        <S extends C> @NonNull Builder<C> senderType(
                @NonNull TypeToken<S> senderType,
                @NonNull Function<S, @Nullable Locale> extractor
        );

        /**
         * Registers a locale extractor for a specific sender type to this builder.
         *
         * <p>Extractors are tried in LIFO registration order. Extractors may return {@code null}
         * to pass the recipient to the next extractor, even when the type matches.</p>
         *
         * @param senderType specific command sender type
         * @param extractor  locale extractor
         * @param <S>        specific command sender type
         * @return this builder
         */
        default <S extends C> @NonNull Builder<C> senderType(
                final @NonNull Class<S> senderType,
                final @NonNull Function<S, @Nullable Locale> extractor
        ) {
            return this.senderType(TypeToken.get(senderType), extractor);
        }

        /**
         * Builds a {@link LocaleExtractor} from the state of this builder.
         *
         * @return new locale extractor
         */
        @NonNull LocaleExtractor<C> build();
    }
}
