import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import essentials.CurrentPlatform;
import fileio.Input;
import java.io.File;
import java.io.IOException;
public class Main {
    /**
     * starts action
     */
    public static void main(final String[] args)throws IOException {
        action(args[0]);
    }
    /**
     * @param filePath1 for input file
     * @throws IOException in case of exceptions to reading / writing
     */

    public static void action(final String filePath1) throws IOException {

        /* read input */
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(filePath1), Input.class);
        ArrayNode output = objectMapper.createArrayNode();

        //starting point to the implementation
        Starter starter = new Starter(inputData);
        starter.startBaby(objectMapper, output);

        //output
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File("results.out"), output);

        //after each test ,reset website to initial
        CurrentPlatform.getInstance().reset();
    }
}

