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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

@API(status = API.Status.INTERNAL)
final class ResourceBundleTranslationBundle<C> implements TranslationBundle<C> {

    private final String key;
    private final LocaleExtractor<C> localeExtractor;
    private final Map<Locale, TranslatedCaptionProvider<C>> translations = new HashMap<>();

    ResourceBundleTranslationBundle(
            final @NonNull String baseName,
            final @NonNull LocaleExtractor<C> localeExtractor
    ) {
        this.key = requireNonNull(baseName, "baseName");
        this.localeExtractor = requireNonNull(localeExtractor, "localeExtractor");
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

    private final class Control extends ResourceBundle.Control {

        @Override
        public List<Locale> getCandidateLocales(final String baseName, final Locale locale) {
            final List<Locale> originalCandidates = super.getCandidateLocales(baseName, locale);
            final String path = ResourceBundleTranslationBundle.this.key.replace(".", "/")
                    .replace("messages", "locales.list");
            final URL url = requireNonNull(this.getClass().getClassLoader().getResource(path), path);
            final List<String> localeStrings;
            try {
                final URLConnection conn = url.openConnection();
                try (InputStream s = conn.getInputStream()) {
                    localeStrings = new BufferedReader(new InputStreamReader(new BufferedInputStream(s)))
                            .lines()
                            .toList();
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            String base = null;
            int noCountry = -1;
            for (int i = 0; i < originalCandidates.size(); i++) {
                final Locale candidate = originalCandidates.get(i);
                if (candidate.getCountry().isBlank()) {
                    base = candidate.getLanguage();
                    noCountry = i + 1;
                    break;
                }
                if (candidate == Locale.ROOT) {
                    return originalCandidates;
                }
            }
            final ArrayList<Locale> locales = new ArrayList<>(originalCandidates);
            if (base != null) {
                for (final String localeString : localeStrings) {
                    if (localeString.startsWith(base + "_")) {
                        locales.add(noCountry, new Locale(localeString));
                    }
                }
            }
            return new ArrayList<>(new LinkedHashSet<>(locales));
        }
    }
}
