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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(status = API.Status.INTERNAL)
final class ResourceBundleTranslationBundle<C> implements TranslationBundle<C> {

    private final String key;
    private final LocaleExtractor<C> localeExtractor;
    private final Map<Locale, TranslatedCaptionProvider<C>> translations = new HashMap<>();

    ResourceBundleTranslationBundle(
            final @NonNull String baseName,
            final @NonNull LocaleExtractor<C> localeExtractor
    ) {
        this.key = Objects.requireNonNull(baseName, "baseName");
        this.localeExtractor = Objects.requireNonNull(localeExtractor, "localeExtractor");
    }

    @Override
    public @NonNull LocaleExtractor<C> localeExtractor() {
        return this.localeExtractor;
    }

    @Override
    public synchronized @Nullable TranslatedCaptionProvider<C> translations(@NonNull final Locale locale) {
        final TranslatedCaptionProvider<C> translatedCaptionProvider = this.translations.computeIfAbsent(
                locale,
                this::loadTranslations
        );
        if (translatedCaptionProvider.isEmpty()) {
            return null;
        }
        return translatedCaptionProvider;
    }

    private @NonNull TranslatedCaptionProvider<C> loadTranslations(final @NonNull Locale locale) {
        try {
            return new ResourceBundleTranslatedCaptionProvider<>(
                    ResourceBundle.getBundle(this.key, locale, new Control()),
                    locale
            );
        } catch (final MissingResourceException ignored) {
            return TranslatedCaptionProvider.empty();
        }
    }

    private static class Control extends ResourceBundle.Control {

        @Override
        public List<Locale> getCandidateLocales(final String baseName, final Locale locale) {
            final List<Locale> originalCandidates = super.getCandidateLocales(baseName, locale);
            System.out.println(originalCandidates);
            return originalCandidates;
        }
    }
}
