package org.mitre.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MarketPropertiesParser {

	// location of market design templates
	private static final String PROP_LOC = "templates/";

	// logger
	private final Logger log = LoggerFactory.getLogger(MarketPropertiesParser.class);

	// the properties class
	private Properties prop;


	/**
	 *
	 * @return MarketPropertiesParser
	 */
	public MarketPropertiesParser() {
		setProp(new Properties());
	}

	/**
	 * @param type of market configuration file title
	 *
	 * @return MarketPropertiesParser
	 */
	public MarketPropertiesParser(String configFile) {
		setProp(new Properties());

		// try to open the properties file
		try (
			FileInputStream fs = new FileInputStream(PROP_LOC + configFile + ".properties")
			){
			getProp().load(fs);
		} catch (IOException e) {
			log.debug("{}", e.toString());
		}
		log.debug("Loaded {} properties file.", configFile);
	}


	/**
	 * @return the prop
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * @param prop the prop to set
	 */
	public void setProp(Properties prop) {
		this.prop = prop;
	}

}
