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
package org.incendo.cloud.translations.bukkit;

import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.caption.CaptionProvider;
import org.incendo.cloud.caption.CaptionRegistry;
import org.incendo.cloud.translations.LocaleExtractor;
import org.incendo.cloud.translations.TranslationBundle;

/**
 * A bundle of Bukkit translations.
 *
 * <p>This can be registered to the command manager by accessing the caption registry using
 * {@link CommandManager#captionRegistry()} and then registering the provider using
 * {@link CaptionRegistry#registerProvider(CaptionProvider)}:
 * <pre>{@code TranslationBundle<C> bundle = BukkitTranslationBundle.bukkit(sender -> sender.locale());
 * manager.captionRegistry().registerProvider(translationBundle);}</pre>
 */
@API(status = API.Status.STABLE)
public final class BukkitTranslationBundle {

    /**
     * Returns the translation bundle for cloud-bukkit translations.
     *
     * @param <C>             command sender type
     * @param localeExtractor locale extractor
     * @return the translation bundle
     */
    public static <C> @NonNull TranslationBundle<C> bukkit(
            final @NonNull LocaleExtractor<C> localeExtractor
    ) {
        return TranslationBundle.resourceBundle("org.incendo.cloud.bukkit.lang.messages", localeExtractor);
    }
}
