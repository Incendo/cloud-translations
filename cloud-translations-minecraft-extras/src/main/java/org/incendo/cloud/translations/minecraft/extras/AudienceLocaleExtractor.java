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
package org.incendo.cloud.translations.minecraft.extras;

import java.util.Locale;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.translations.LocaleExtractor;

import static java.util.Objects.requireNonNull;

@DefaultQualifier(NonNull.class)
public final class AudienceLocaleExtractor<C> implements LocaleExtractor<C>, Function<C, @Nullable Locale> {

    private final AudienceProvider<C> audienceProvider;
    private final LocaleExtractor<C> fallback;

    private AudienceLocaleExtractor(
            final AudienceProvider<C> audienceProvider,
            final LocaleExtractor<C> fallback
    ) {
        this.audienceProvider = requireNonNull(audienceProvider, "audienceProvider");
        this.fallback = requireNonNull(fallback, "fallback");
    }

    /**
     * Returns a locale extractor that uses the {@link Identity#LOCALE locale pointer} to extract a {@link Locale} from
     * the sender as an audience as mapped by {@code audienceProvider}.
     *
     * @param audienceProvider audience provider
     * @param <C>              command sender type
     * @return extractor
     */
    public static <C> AudienceLocaleExtractor<C> audienceLocaleExtractor(final AudienceProvider<C> audienceProvider) {
        return new AudienceLocaleExtractor<>(audienceProvider, $ -> Locale.getDefault());
    }

    /**
     * Returns a locale extractor that uses the {@link Identity#LOCALE locale pointer} to extract a {@link Locale} from
     * the sender.
     *
     * @param <C> command sender type
     * @return extractor
     */
    public static <C extends Audience> AudienceLocaleExtractor<C> audienceLocaleExtractor() {
        return audienceLocaleExtractor(AudienceProvider.nativeAudience());
    }

    @Override
    public Locale extract(final C recipient) {
        return this.audienceProvider.apply(recipient).get(Identity.LOCALE).orElseGet(() -> this.fallback.extract(recipient));
    }

    @Override
    public @Nullable Locale apply(final C recipient) {
        return this.audienceProvider.apply(recipient).get(Identity.LOCALE).orElse(null);
    }

    /**
     * Returns a copy of this extractor that falls back to the provided extractor when the {@link Identity#LOCALE locale pointer}
     * does not have a value for a given audience.
     *
     * <p>Note that this only applies to {@link #extract(Object)}. {@link #apply(Object)} will always return {@code null}
     * when the pointer does not have a value, as it is meant for use with {@link LocaleExtractor.Builder}</p>
     *
     * @param fallback fallback extractor
     * @return defaulted extractor
     */
    public AudienceLocaleExtractor<C> defaulted(final @NonNull LocaleExtractor<C> fallback) {
        return new AudienceLocaleExtractor<>(this.audienceProvider, fallback);
    }
}
