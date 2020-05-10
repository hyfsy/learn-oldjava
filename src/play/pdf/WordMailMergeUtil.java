package play.pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import com.aspose.words.Document;
import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.License;
import com.aspose.words.MailMerge;
import com.aspose.words.MailMergeCleanupOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import com.aspose.words.ref.Ref;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WordMailMergeUtil
{

    private String licenseName = "resources/license.lic";

    public WordMailMergeUtil() {

    }

    public WordMailMergeUtil(String licenseName) {
        this.licenseName = licenseName;
    }

    public boolean applyWordLicense(String licenseName) {
        boolean result = false;

        try {
            License license = new License();
            license.setLicense(licenseName);

            result = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void mergeWordTemplate(Object template, Object output, JSONObject data, com.aspose.words.SaveOptions saveOptions) {
        if (template == null || output == null) {
            return;
        }
        try {

            if (!applyWordLicense(this.licenseName)) {
                return;
            }

            Document doc = null;

            if (template.getClass() == String.class) {
                doc = new Document((String) template);
            }
            else if (template.getClass() == Document.class) {
                doc = (Document) template;
            }
            else {
                doc = new Document((InputStream) template);
            }

            MailMerge mailMerge = doc.getMailMerge();
            mailMerge.setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
            mailMerge.executeWithRegions(new NormalTableDataSource(data));

            if (output.getClass() == String.class) {
                if (saveOptions != null && ImageSaveOptions.class == saveOptions.getClass()) {
                    ImageSaveOptions imageSaveOptions = (ImageSaveOptions) saveOptions;
                    String fileFullPath = (String) output;

                    for (int i = 0, count = doc.getPageCount(); i < count; i++) {
                        String index = "00" + i;
                        index = index.substring(index.length() - 3);

                        imageSaveOptions.setPageIndex(i);

                        doc.save(fileFullPath + index + ".jpg", saveOptions);
                    }
                }
                else {
                    doc.save((String) output, saveOptions);
                }
            }
            else {
                if (saveOptions == null) {
                    saveOptions = SaveOptions.createSaveOptions(SaveFormat.DOCX);
                }

                doc.save((OutputStream) output, saveOptions);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class NormalTableDataSource implements IMailMergeDataSource
    {

        private int childIndex = -1;

        private JSONObject data = null;

        private JSONObject currentChild = null;

        public NormalTableDataSource(JSONObject input) {
            this.data = input;

            if (this.data == null) {
                this.data = new JSONObject();
            }

        }

        @Override
        public IMailMergeDataSource getChildDataSource(String name) throws Exception {
            if (this.currentChild != null && this.currentChild.has("children")) {
                JSONArray children = this.currentChild.getJSONArray("children");
                if (children != null && children.size() > 0) {
                    for (int i = 0, size = children.size(); i < size; i++) {
                        if (name.equals(children.getJSONObject(i).get("tableName"))) {
                            return new NormalTableDataSource(children.getJSONObject(i));
                        }
                    }
                }

            }

            return null;
        }

        @Override
        public String getTableName() throws Exception {
            Object tableName = this.data.get("tableName");
            return tableName == null ? null : tableName.toString();
        }

        @Override
        public boolean getValue(String field, Ref<Object> ref) throws Exception {
            Object value = this.currentChild.get(field);
            if (!(value == null || "".equals(value))) {
                value = URLDecoder.decode(value.toString(), "utf-8");
            }

            ref.set(value);

            return true;
        }

        @Override
        public boolean moveNext() throws Exception {
            if (!this.data.has("rows")) {
                return false;
            }

            JSONArray children = this.data.getJSONArray("rows");
            if (children != null && this.childIndex < children.size() - 1) {
                this.childIndex++;

                this.currentChild = children.getJSONObject(this.childIndex);

                return true;
            }

            return false;
        }
    }

}
