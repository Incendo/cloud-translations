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
import java.util.Objects;
import java.util.ResourceBundle;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.caption.Caption;

@API(status = API.Status.INTERNAL)
final class ResourceBundleTranslatedCaptionProvider<C> implements TranslatedCaptionProvider<C> {

    private final ResourceBundle resourceBundle;
    private final Locale locale;

    ResourceBundleTranslatedCaptionProvider(
            final @NonNull ResourceBundle resourceBundle,
            final @NonNull Locale locale
    ) {
        this.resourceBundle = Objects.requireNonNull(resourceBundle, "resourceBundle");
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    @Override
    public boolean isEmpty() {
        return this.resourceBundle.keySet().isEmpty();
    }

    @Override
    public @NonNull Locale locale() {
        return this.locale;
    }

    @Override
    public @Nullable String provide(final @NonNull Caption caption, final @NonNull C recipient) {
        if (this.resourceBundle.containsKey(caption.key())) {
            return this.resourceBundle.getString(caption.key());
        }
        return null;
    }
}
