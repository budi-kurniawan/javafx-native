package com.brainysoftware.pdfeditor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PropertyManager {
	
    private static final String RECENT_DOCUMENT_KEY = "recent";
	private static PropertyManager instance;
	private Path propertiesPath;
	private PropertyManager() {
        String homeDir = System.getProperty("user.home");
        propertiesPath = Paths.get(homeDir, "pdf-editor.properties");
        createPropertiesFile();
	}
	
	public void createPropertiesFile() {
        if (!Files.exists(propertiesPath)) {
        	try {
				Files.createFile(propertiesPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	/**
	 * Add a property to the properties file
	 * @param key
	 * @param value
	 * @param uniqueKey. Indicates whether the key must be unique. A value of false allows
	 * the presence of multiple properties sharing the same key.
	 */
	public void setProperty(String key, String value, boolean uniqueKey) {
		// read all properties from the current properties file, delete the properties file,
		// create a new properties file, and rewrite all eligible lines to the new file
		List<String> newLines = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(propertiesPath);
			for (String line : lines) {
				if (!line.trim().startsWith("#") && uniqueKey) {
					int index = line.indexOf('=');
					if (index != -1) {
						String existingKey = line.substring(0, index).trim();
						if (key.equals(existingKey)) {
							continue;								
						}
					}						
				}
				newLines.add(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		newLines.add(key + "=" + value);
		resetPropertiesFile();
		try (FileWriter fileWriter = new FileWriter(propertiesPath.toFile())) {
			for (String line : newLines) {
				fileWriter.write(line + "\n");				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public String getProperty(String key) {
		try {
			List<String> lines = Files.readAllLines(propertiesPath);
			for (String line : lines) {
				int index = line.indexOf('=');
				if (index != -1) {
					String existingKey = line.substring(0, index).trim();
					if (key.equals(existingKey)) {
						return line.substring(index + 1).trim();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Deletes the current properties file and create an empty one
	 */
	public void resetPropertiesFile() {
		try {
			Files.delete(propertiesPath);
			createPropertiesFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetPropertiesFileWithProperties(List<String> newLines) {
	    resetPropertiesFile();
	    try (FileWriter fileWriter = new FileWriter(propertiesPath.toFile())) {
            for (String line : newLines) {
                fileWriter.write(line + "\n");              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }       
	}
	
	/**
	 * Replace this with getProperties()
	 * @return
	 */
	public List<String> getRecentDocuments() {
		List<String> recentDocuments = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(propertiesPath);
			lines.forEach(line -> {
				int index = line.indexOf('=');
				if (index != -1) {
					String key = line.substring(0, index).trim();
					String value = line.substring(index + 1).trim();
					if (RECENT_DOCUMENT_KEY.equals(key)) {
						recentDocuments.add(value);
					}					
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return recentDocuments;
	}
	
	public void removeFromRecentDocuments(Path path) {
	    List<String> newLines = new ArrayList<>();
        try {
            Files.readAllLines(propertiesPath).forEach(line -> {
                if (!line.equals(RECENT_DOCUMENT_KEY + "=" + path.toString())) {
                    newLines.add(line);
                }
            });
            resetPropertiesFileWithProperties(newLines);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}

   public void setAsFirstRecentDocument(Path path) {
        List<String> newLines = new ArrayList<>();
        newLines.add(RECENT_DOCUMENT_KEY + "=" + path.toString());
        try {
            Files.readAllLines(propertiesPath).forEach(line -> {
                if (!line.equals(RECENT_DOCUMENT_KEY + "=" + path.toString())) {
                    newLines.add(line);
                }
            });
            resetPropertiesFileWithProperties(newLines);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
   
   public String getLastOpenDir() {
       return getProperty(Constants.PROPERTY_LAST_OPENED_DIR);
   }
   
   public void setLastOpenDir(String lastOpenedDir) {
       setProperty(Constants.PROPERTY_LAST_OPENED_DIR, lastOpenedDir, /*uniqueKey=*/ true);
   }

	public static PropertyManager getInstance() {
		synchronized(PropertyManager.class) {
			if (instance == null) {
				instance = new PropertyManager();			
			}
		}
		return instance;
	}
}
