package wolf.work.proj.front;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.*;
import wolf.work.proj.lab.Record;

public class Terminal extends Stage {
    private TextArea textArea;
    private final String HELP_TEXT = "Use \"quantity\" with parameter l/i to " +
            "get object quantity!\n";
    private final String UNKNOWN_TEXT = "Unknown command. Use \"help\" to see all available commands.\n";

    private PipedInputStream commandInput = new PipedInputStream();
    private PipedOutputStream commandOutput = new PipedOutputStream();
    PrintWriter commandWriter = new PrintWriter(commandOutput, true);

    public Terminal() {
        textArea = new TextArea();
        textArea.setText("> ");
        setMinHeight(600);
        setMinWidth(800);

        try {
            commandInput.connect(commandOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(commandInput))) {
                String command;
                while ((command = reader.readLine()) != null) {
                    final String cmd = command;
                    String result = processCommand(cmd);
                    Platform.runLater(() -> {
                        textArea.appendText(result + "\n> ");
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();

                String fullText = textArea.getText();
                int lastPrompt = fullText.lastIndexOf("> ");
                String command = fullText.substring(lastPrompt + 2).trim();

                if (!command.isEmpty()) {
                    commandWriter.println(command);
                    commandWriter.flush();
                }
            }
        });
        setOnCloseRequest(event -> closeIO());
        Scene scene = new Scene(textArea);
        setTitle("Terminal");
        setScene(scene);
    }

    private String processCommand(String command) {
        if (command.equalsIgnoreCase("help")) {
            return HELP_TEXT;
        }
        else if (command.equalsIgnoreCase("quantity i")) {
            return "Individual records quantity: " + Record.indCountAlive;
        }
        else if (command.equalsIgnoreCase("quantity l")) {
            return "Legal records quantity: " + Record.legCountAlive;
        }
        else {
            return UNKNOWN_TEXT;
        }
    }

    public void closeIO() {
        System.out.println("Terminal closed");
        commandWriter.close();
        try {
            commandOutput.close();
            commandInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}