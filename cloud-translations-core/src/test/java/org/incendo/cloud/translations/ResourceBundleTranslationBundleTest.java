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
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.StandardCaptionKeys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceBundleTranslationBundleTest {

    @Mock
    private CommandSender commandSender;

    @Test
    void testCoreBundle() {
        // Arrange
        when(this.commandSender.locale()).thenReturn(Locale.ENGLISH);

        // Act
        final TranslationBundle<CommandSender> bundle = TranslationBundle.core(CommandSender::locale);

        // Assert
        assertThat(bundle.provide(StandardCaptionKeys.EXCEPTION_INVALID_SYNTAX, this.commandSender))
                .isEqualTo("Invalid command syntax. Correct command syntax is: <syntax>.");
    }

    @Test
    void testMissingTranslationReturnsNull() {
        // Arrange
        when(this.commandSender.locale()).thenReturn(Locale.ENGLISH);

        // Act
        final TranslationBundle<CommandSender> bundle = TranslationBundle.core(CommandSender::locale);

        // Assert
        assertThat(bundle.provide(Caption.of("invalid"), this.commandSender)).isEqualTo(null);
    }

    @Test
    void fallbackLocale() {
        final TranslationBundle<CommandSender> bundle = TranslationBundle.resourceBundle(
                "org.incendo.cloud.translations.test.fallback_locale.messages",
                CommandSender::locale
        );

        when(this.commandSender.locale()).thenReturn(new Locale("es", "MX", ""));

        assertThat(bundle.provide(Caption.of("present"), this.commandSender)).isEqualTo("ES_MX");
        assertThat(bundle.provide(Caption.of("present1"), this.commandSender)).isEqualTo("ES_ES1");
        assertThat(bundle.provide(Caption.of("missing"), this.commandSender)).isEqualTo("ENGLISH FALLBACK");
    }

    interface CommandSender {

        @NonNull Locale locale();
    }
}
