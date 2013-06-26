package org.apache.avro.tool;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaValidationException;
import org.apache.avro.SchemaValidator;
import org.apache.avro.SchemaValidatorBuilder;

/**
 * Utility to induce a schema from a class or a protocol from an interface.
 */
public class SchemaValidatorTool implements Tool {

  @Override
  public int run(InputStream in, PrintStream out, PrintStream err,
                 List<String> args) throws Exception {
    int returnCode = 0;

    if (args.size() < 2) {
      System.err
          .println("Usage: newschema oldschemas...");
      System.err
          .println(" oldSchemas - old schemas to be validated against");
      System.err
          .println(" newSchema - the new schema to check backwards compatibility");
      return 1;
    }


    Schema newSchema = new Schema.Parser().parse(new File(args.get(0)));

    List<Schema> priors = new ArrayList<Schema>();
    for (int i = 1; i < args.size() - 1; i++) {
      priors.add(new Schema.Parser().parse(new File(args.get(i))));
    }

    SchemaValidator validator = new SchemaValidatorBuilder().quixeyNothingRemoved().validateAll();

    try {
      validator.validate(newSchema, priors);
      System.out.println("Valid!");
    } catch (SchemaValidationException e) {
      System.out.println("Not Valid!");
      returnCode = 1;
    }

    return returnCode;
  }

  @Override
  public String getName() {
    return "validate";
  }

  @Override
  public String getShortDescription() {
    return "Validates new schemas against old.";
  }
}
