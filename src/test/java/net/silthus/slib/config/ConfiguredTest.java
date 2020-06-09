package net.silthus.slib.config;

import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.configs.yaml.YamlConfiguration;
import lombok.NonNull;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Configured")
class ConfiguredTest {

    @Nested
    @DisplayName("isConfigType(Object config)")
    class isConfigType {

        @Test
        @DisplayName("should return false if config object is null")
        void falseIfNull() {
            assertThat(new TestConfigured().isConfigType(null))
                    .isFalse();
        }

        @Test
        @DisplayName("should return true if the direct type matches")
        void shouldMatchType() {
            assertThat(new TestConfigured().isConfigType(new TestConfig()))
                    .isTrue();
        }

        @Test
        @DisplayName("should return true if super type matches")
        void shouldMatchSuperType() {
            assertThat(new TestConfiguredSuper().isConfigType(new TestConfig()))
                    .isTrue();
        }
    }

    @Nested
    @DisplayName("tryLoad(Object config)")
    class tryLoad {

        @Test
        @SuppressWarnings("unchecked")
        @DisplayName("should call load(TConfig) if isConfigType is true")
        void onlyCallsLoadIfIsConfigTypeReturnsTrue() {

            Configured<String> configured = (Configured<String>) mock(Configured.class);
            when(configured.isConfigType(any())).thenReturn(true);
            when(configured.getConfigClass()).thenReturn(String.class);
            doAnswer(InvocationOnMock::callRealMethod)
                    .when(configured)
                    .tryLoad(any());

            configured.tryLoad("");

            verify(configured, times(1)).load("");
        }

        @Test
        @SuppressWarnings("unchecked")
        @DisplayName("should not call load(TConfig) if isConfigType is false")
        void shouldNotCallLoadIfConfigTypeIsFalse() {

            Configured<String> configured = (Configured<String>) mock(Configured.class);
            when(configured.isConfigType(any())).thenReturn(false);
            when(configured.getConfigClass()).thenReturn(String.class);
            doAnswer(InvocationOnMock::callRealMethod)
                    .when(configured)
                    .tryLoad(any());

            configured.tryLoad("");

            verify(configured, times(0)).load("");
        }
    }


    class TestConfigured implements Configured<TestConfig> {
        @Override
        public Class<TestConfig> getConfigClass() {
            return TestConfig.class;
        }

        @Override
        public void load(@NonNull TestConfig config) {

        }
    }

    class TestConfiguredSuper implements Configured<YamlConfiguration> {
        @Override
        public Class<YamlConfiguration> getConfigClass() {
            return YamlConfiguration.class;
        }

        @Override
        public void load(@NonNull YamlConfiguration yamlConfiguration) {

        }
    }


    class TestConfig extends BukkitYamlConfiguration {

        protected TestConfig(Path path, BukkitYamlProperties properties) {
            super(path, properties);
        }

        protected TestConfig(Path path) {
            super(path);
        }

        protected TestConfig() {
            super(new File(RandomStringUtils.randomAlphanumeric(5)).toPath());
        }
    }
}