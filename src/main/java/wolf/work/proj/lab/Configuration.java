package wolf.work.proj.lab;

import wolf.work.proj.front.SimController;

import java.io.*;
import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties = new Properties();
    public void writeConfig() {
        properties.setProperty("legal.period", String.valueOf(Habitat.LEGAL_PERIOD));
        properties.setProperty("individual.period", String.valueOf(Habitat.INDIVIDUAL_PERIOD));
        properties.setProperty("legal.lifespan", String.valueOf(Habitat.LEGAL_LIFESPAN));
        properties.setProperty("individual.lifespan", String.valueOf(Habitat.INDIVIDUAL_LIFESPAN));
        properties.setProperty("legal.chance", String.valueOf(Habitat.LEGAL_SPAWN_CHANCE));
        properties.setProperty("individual.chance", String.valueOf(Habitat.INDIVIDUAL_SPAWN_CHANCE));
        properties.setProperty("legal.priority.ai", String.valueOf(Habitat.LEGAL_AI_PRIORITY));
        properties.setProperty("individual.priority.ai", String.valueOf(Habitat.INDIVIDUAL_AI_PRIORITY));

        properties.setProperty("show.info.state", String.valueOf(Habitat.SHOW_INFO_STATE));
        properties.setProperty("show.time.state", String.valueOf(Habitat.SHOW_TIME_STATE));
        properties.setProperty("legal.ai.on", String.valueOf(Habitat.LEGAL_AI_ON));
        properties.setProperty("individual.ai.on", String.valueOf(Habitat.INDIVIDUAL_AI_ON));

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            properties.store(writer, "Simulation Configuration");
            System.out.println("Config written");
        }
        catch (IOException e) {
            System.err.println("Config writing failed");
        }
    }
    public void readConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {




            properties.load(reader);

            Habitat.LEGAL_PERIOD = Integer.parseInt(properties.getProperty("legal.period"));
            Habitat.INDIVIDUAL_PERIOD = Integer.parseInt(properties.getProperty("individual.period"));
            Habitat.LEGAL_LIFESPAN = Integer.parseInt(properties.getProperty("legal.lifespan"));
            Habitat.INDIVIDUAL_LIFESPAN = Integer.parseInt(properties.getProperty("individual.lifespan"));
            Habitat.LEGAL_SPAWN_CHANCE = Double.parseDouble(properties.getProperty("legal.chance"));
            Habitat.INDIVIDUAL_SPAWN_CHANCE = Double.parseDouble(properties.getProperty("individual.chance"));
            Habitat.LEGAL_AI_PRIORITY = Integer.parseInt(properties.getProperty("legal.priority.ai"));
            Habitat.INDIVIDUAL_AI_PRIORITY = Integer.parseInt(properties.getProperty("individual.priority.ai"));

            Habitat.SHOW_INFO_STATE = Boolean.parseBoolean(properties.getProperty("show.info.state"));
            Habitat.SHOW_TIME_STATE = Boolean.parseBoolean(properties.getProperty("show.time.state"));
            Habitat.LEGAL_AI_ON = Boolean.parseBoolean(properties.getProperty("legal.ai.on"));
            Habitat.INDIVIDUAL_AI_ON = Boolean.parseBoolean(properties.getProperty("individual.ai.on"));

            System.out.println("Config read successfully");

        } catch (FileNotFoundException e) {
            System.out.println("no file, using default values");
        } catch (IOException e) {
            System.err.println("failed to load config " + e.getMessage());
        }
    }
}
