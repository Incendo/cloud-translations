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

import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.type.tuple.Pair;

final class LocaleExtractorBuilderImpl<C> implements LocaleExtractor.Builder<C> {

    private @NonNull LocaleExtractor<C> fallback = $ -> Locale.getDefault();
    private final LinkedList<Pair<Type, Function<Object, @Nullable Locale>>> extractors = new LinkedList<>();

    @Override
    public LocaleExtractor.@NonNull Builder<C> fallback(final @NonNull LocaleExtractor<C> fallback) {
        this.fallback = Objects.requireNonNull(fallback);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <S extends C> LocaleExtractor.@NonNull Builder<C> senderType(
            final @NonNull TypeToken<S> senderType,
            final @NonNull Function<S, @Nullable Locale> extractor
    ) {
        this.extractors.addFirst(Pair.of(senderType.getType(), (Function) extractor));
        return this;
    }

    @Override
    public @NonNull LocaleExtractor<C> build() {
        final LocaleExtractor<C> fallback = this.fallback;
        final List<Pair<Type, Function<Object, Locale>>> extractors = List.copyOf(this.extractors);
        return recipient -> {
            for (final Pair<Type, Function<Object, Locale>> pair : extractors) {
                if (GenericTypeReflector.isSuperType(pair.first(), recipient.getClass())) {
                    final @Nullable Locale apply = pair.second().apply(recipient);
                    if (apply != null) {
                        return apply;
                    }
                }
            }
            return fallback.extract(recipient);
        };
    }
}
