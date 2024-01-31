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
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionProvider;
import org.incendo.cloud.caption.CaptionRegistry;

/**
 * A bundle of translations.
 *
 * <p>This can be registered to the command manager by accessing the caption registry using
 * {@link CommandManager#captionRegistry()} and then registering the provider using
 * {@link CaptionRegistry#registerProvider(CaptionProvider)}:
 * <pre>{@code TranslationBundle<C> bundle = TranslationBundle.core(sender -> sender.locale());
 * manager.captionRegistry().registerProvider(translationBundle);}</pre>
 *
 * @param <C> command sender type
 */
@API(status = API.Status.STABLE)
public interface TranslationBundle<C> extends CaptionProvider<C> {

    /**
     * Creates a new translation bundle backed by a{@link java.util.ResourceBundle} with the given {@code baseName}.
     *
     * @param <C>             command sender type
     * @param baseName        resource bundle base name
     * @param localeExtractor locale extractor
     * @return the translation bundle
     */
    static <C> @NonNull TranslationBundle<C> resourceBundle(
            final @NonNull String baseName,
            final @NonNull LocaleExtractor<C> localeExtractor
    ) {
        return new ResourceBundleTranslationBundle<>(baseName, localeExtractor);
    }

    /**
     * Returns the translation bundle for the cloud-core translations.
     *
     * @param <C>             command sender type
     * @param localeExtractor locale extractor
     * @return the translation bundle
     */
    static <C> @NonNull TranslationBundle<C> core(
            final @NonNull LocaleExtractor<C> localeExtractor
    ) {
        return new ResourceBundleTranslationBundle<>("org.incendo.cloud.core.lang.messages", localeExtractor);
    }

    /**
     * Returns the extractor that extract locales from senders of type {@link C}.
     *
     * @return locale extractor
     */
    @NonNull LocaleExtractor<C> localeExtractor();

    /**
     * Returns the translations for the given {@code locale}.
     *
     * @param locale translation locale
     * @return the translations, or {@code null} if the given {@code locale} is not supported
     */
    @Nullable TranslatedCaptionProvider<C> translations(@NonNull Locale locale);

    @Override
    default @Nullable String provide(final @NonNull Caption caption, final @NonNull C recipient) {
        final Locale locale = this.localeExtractor().extract(recipient);
        final TranslatedCaptionProvider<C> translatedCaptionProvider = this.translations(locale);
        if (translatedCaptionProvider == null) {
            return null;
        }
        return translatedCaptionProvider.provide(caption, recipient);
    }
}
