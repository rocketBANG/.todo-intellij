import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Reuben on 12/09/2017.
 */
public class TestAction extends AnAction {

    @Override
    public void update(final AnActionEvent e) {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final VirtualFile openedFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String fileExt = openedFile.getExtension();

        if(fileExt == null || !(fileExt.equals("tasks") || fileExt.equals("todos"))) {
            return;
        }
        
        System.out.println("running");

        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        int lineStart = document.getLineStartOffset(document.getLineNumber(start));
        String startingText = document.getText(TextRange.create(lineStart, lineStart + 3));

        String replacementText;

        if(startingText.equals("[x]")) {
            replacementText = "[ ]";
        }
        else if(startingText.equals("[ ]")) {
            replacementText = "[x]";
        }
        else {
            return;
        }

        //New instance of Runnable to make a replacement
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.replaceString(lineStart, lineStart + 3, replacementText);
            }
        };
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();


    }
}
