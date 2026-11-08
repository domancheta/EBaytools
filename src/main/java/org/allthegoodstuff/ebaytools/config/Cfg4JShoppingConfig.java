package org.allthegoodstuff.ebaytools.config;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.Arrays;

public class Cfg4JShoppingConfig implements ShoppingConfig {

    private ConfigurationProvider configProvider;

    @Inject
    public Cfg4JShoppingConfig() {
        // TODO: probably move config file somewhere in class structure and use the Cfg4j classpath provider instead
        ConfigFilesProvider configFilesProvider = () -> Arrays.asList(Paths.get("config.yaml"));
        ConfigurationSource configSource = new FilesConfigurationSource(configFilesProvider);
        Environment env = new ImmutableEnvironment(".");

        configProvider = new ConfigurationProviderBuilder().withConfigurationSource(configSource)
                .withEnvironment(env)
                .build();

        //todo: no exception thrown if the yaml is invalid, e.g., just adding ebay by itself in a line with no colon
        // I think the code is just returning and breaks the builder, and line to fetch the item isn't reached
        // can try putting in a validator if they don't fix this - there is a kwalify yaml validator out there

    }

    public String mainTableName() {
        return configProvider.getProperty("mainTableName", String.class);
    }

    public String dbDriverName() {
        return configProvider.getProperty("dbDriverName", String.class);
    }

    public String logoImageFilename() {
        return configProvider.getProperty("logoImageFilename", String.class);
    }

    public String baseApiUri() {
       return configProvider.getProperty("baseApiUri", String.class);
    }

    public String apiVersion() {
        return configProvider.getProperty("apiVersion", String.class);
    }

    public String appID() {
        return configProvider.getProperty("appid", String.class);
    }

    public String certID() {
        return configProvider.getProperty("certid", String.class);
    }

    public String callname() {
        return configProvider.getProperty("getItemCallname", String.class);
    }

    public String itemStatusCallname() {
        return configProvider.getProperty("getItemStatusCallname", String.class);
    }

    public String rsEncoding() {
        return configProvider.getProperty("rsEncoding", String.class);
    }

    public String siteid() {
        return configProvider.getProperty("siteid", String.class);
    }

    public String includeSelector(){
        return configProvider.getProperty("includeDetailsSelector", String.class);
    }

    // generic get to retrieve any custom property in config file(s)
    public String getProperty(String key) {
        return configProvider.getProperty(key, String.class);
    }

}
