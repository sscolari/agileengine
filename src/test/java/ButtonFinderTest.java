import com.agileengine.ButtonFinder;
import junit.framework.TestCase;

/**
 * Created by santiagoscolari on 29/10/18.
 */
public class ButtonFinderTest extends TestCase {

    private String original = "src/main/resources/samples/sample-0-origin.html";
    private String sample1 = "src/main/resources/samples/sample-1-evil-gemini.html";
    private String sample2 = "src/main/resources/samples/sample-2-container-and-clone.html";
    private String sample3 = "src/main/resources/samples/sample-3-the-escape.html";
    private String sample4 = "src/main/resources/samples/sample-4-the-mash.html";

    public void testCase1() {
        String[] args = {original, sample1};
        ButtonFinder.main(args);
    }

    public void testCase2() {
        String[] args = {original, sample2};
        ButtonFinder.main(args);
    }

    public void testCase3() {
        String[] args = {original, sample3};
        ButtonFinder.main(args);
    }

    public void testCase4() {
        String[] args = {original, sample4};
        ButtonFinder.main(args);
    }

}
