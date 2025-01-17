package io.github.erha134.mc.sparklib.data;

import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.test.TestLanguageProvider;
import io.github.erha134.mc.sparklib.test.TestTagProvider;

public class SparkLibDataGeneration {
    public static void initDataGen(SDataGeneration dataGeneration) {
        if (Platform.isDevelopmentEnvironment()) {
            dataGeneration.addProvider(TestLanguageProvider::new);
            dataGeneration.addProvider(TestTagProvider::new);
        }
        dataGeneration.run();
    }
}
