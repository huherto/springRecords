package com.github.springyRecords.generator;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class BaseTableClassWriter extends BaseClassWriter {

	public void makeClass(Path sourceDir, TableTool tableTool) {
	    try {
	    	File sourceFile =
	    		sourceFile(sourceDir,
	    				tableTool.baseTablePackageName(),
	    				tableTool.baseTableClassName());
	        if (sourceFile.exists()) {
	            sourceFile.delete();
	            sourceFile.createNewFile();
	        }
	        writeCode(sourceFile, createTemplate(), tableTool);
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("basetable.mustache");
    }


}
