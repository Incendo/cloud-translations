package org.incendo.cloud.translations;

import java.util.Locale;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class LocaleExtractorBuilderTest {

    @Test
    void testSimple() {
        final LocaleExtractor<GenericSender> build = LocaleExtractor.<GenericSender>builder()
                .fallback(Locale.FRENCH)
                .senderType(ChineseSender.class, s -> s.locale)
                .senderType(GermanSender.class, s -> s.locale)
                .build();

        assertThat(build.extract(new GenericSender())).isEqualTo(Locale.FRENCH);
        assertThat(build.extract(new ChineseSender())).isEqualTo(Locale.CHINESE);
        assertThat(build.extract(new GermanSender())).isEqualTo(Locale.GERMAN);
    }

    static class GenericSender {

    }

    static class ChineseSender extends GenericSender {

        final Locale locale = Locale.CHINESE;
    }

    static class GermanSender extends GenericSender {

        final Locale locale = Locale.GERMAN;
    }
}
