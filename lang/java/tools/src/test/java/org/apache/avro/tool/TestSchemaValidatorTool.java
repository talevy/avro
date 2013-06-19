package org.apache.avro.tool;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestSchemaValidatorTool {


  // where test input/expected schemas comes from
  private static final File TEST_DIR =
      new File(System.getProperty("test.validator.schema.dir", "src/test/validator"));

  // where old schemas come from
  private static final File TEST_OLD_SCHEMAS_DIR =
      new File(TEST_DIR, "old-schemas");

  // where new schemas come from
  private static final File TEST_NEW_SCHEMAS_DIR =
      new File(TEST_DIR, "new-schemas");

  @Test
  public void testValidSchemaOneFile() throws Exception {
    assertEquals(0, doValidate(new String[]{
        TEST_NEW_SCHEMAS_DIR.toString() + "/newschema.avsc",
        TEST_OLD_SCHEMAS_DIR.toString() + "/sample-old.avsc",
    }));
  }

  @Test
  public void testValidSchemaTwoFiles() throws Exception {
    assertEquals(0, doValidate(new String[]{
        TEST_NEW_SCHEMAS_DIR.toString() + "/newschema.avsc",
        TEST_OLD_SCHEMAS_DIR.toString() + "/sample-older.avsc",
        TEST_OLD_SCHEMAS_DIR.toString() + "/sample-old.avsc",
    }));
  }

  @Test
  public void testInvalidSchemaOneFile() throws Exception {
    assertEquals(1, doValidate(new String[]{
        TEST_NEW_SCHEMAS_DIR.toString() + "/newinvalidschema.avsc",
        TEST_OLD_SCHEMAS_DIR.toString() + "/sample-older.avsc",
        TEST_OLD_SCHEMAS_DIR.toString() + "/sample-old.avsc",
    }));
  }

  // Runs the actual validator tool with the given input args
  private int doValidate(String[] args) throws Exception {
    SchemaValidatorTool tool = new SchemaValidatorTool();
    return tool.run(null, null, null, Arrays.asList((args)));
  }
}
