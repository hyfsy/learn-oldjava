package test.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件随机重命名
 *
 * @author baB_hyf
 * @date 2022/04/26
 */
public class FileNameReplacer {

    public static void main(String[] args) {
        String dir = "C:\\Users\\baB_hyf\\Desktop\\song";
        replace(dir);
    }

    public static void replace(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        replaceDir(dir);
    }

    private static void replaceDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        List<File> stores = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                replaceDir(file);
            }
            else {
                stores.add(file);
            }
        }

        replaceFiles(stores);
    }

    private static void replaceFiles(List<File> files) {
        if (files.isEmpty()) {
            return;
        }

        // 删除无用图片
        removeFile(files, (file, name, suffix) -> "png".equals(suffix) && (name.contains("_title_") || name.contains("_ipad")));

        // 删除其他无用文件
        removeFile(files, ((file, name, suffix) -> !("png".equals(suffix) || "mp3".equals(suffix) || "imd".equals(suffix))));

        File dir = files.get(0).getParentFile();

        List<String> fileNameList = files.stream().map(File::getName).collect(Collectors.toList());

        List<String> _4kImdFileNames = fileNameList.stream()
                .filter(f -> f.contains(".imd"))
                .filter(f -> !f.contains("5k") && !f.contains("6k"))
                .collect(Collectors.toList());

        // 没有4k的，保留重要的文件供查看
        if (_4kImdFileNames.isEmpty()) {
            removeFile(files, (file, name, suffix) -> !("png".equals(suffix) || "mp3".equals(suffix)));
            if (!dir.getName().contains("_nothing")) {
                renameFile(dir, dir.getName() + "_nothing");
            }
            return;
        }

        // 处理4k
        String singleImdFileName = _4kImdFileNames.stream().filter(f -> f.contains("_hd")).findFirst()
                .orElseGet(() -> _4kImdFileNames.stream().filter(f -> f.contains("_nm")).findFirst()
                        .orElseGet(() -> _4kImdFileNames.stream().filter(f -> f.contains("_ez")).findFirst()
                                .orElse(null)));

        // 删除其他4k
        removeFile(files, (file, name, suffix) -> "imd".equals(suffix) && (singleImdFileName == null || !singleImdFileName.equals(name)));

        // 所有文件重命名
        for (File file : files) {
            if (!file.getName().contains("_m_")) {
                renameFile(file, getRandomFileName(file));
            }
        }
    }

    private static void removeFile(List<File> files, FilePredicate filePredicate) {
        files.removeIf(f -> {
            String name = f.getName();
            String suffix = name.substring(name.lastIndexOf('.') + 1);

            if (filePredicate.test(f, name, suffix)) {
                if (!f.delete()) {
                    System.out.println("file delete failed: " + f.getAbsolutePath());
                }
                return true;
            }
            return false;
        });


    }

    private static File renameFile(File file, String newName) {
        File newFile = new File(file.getParentFile(), newName);
        if (!file.renameTo(newFile)) {
            String word = "rename file failed: " + file.getAbsolutePath() + " -> " + newFile.getAbsolutePath();
            // throw new RuntimeException(word);
            System.out.println(word);
        }
        return newFile;
    }

    private static String getRandomFileName(File file) {
        String fileName = file.getName();
        StringBuilder sb = new StringBuilder(fileName);
        int i = sb.lastIndexOf(".");
        if (i == -1) {
            throw new RuntimeException("illegal file name: " + file.getAbsolutePath());
        }
        String random = String.valueOf(System.currentTimeMillis()).substring(8);
        sb.insert(i, "_m_" + random);
        return sb.toString();
    }

    @FunctionalInterface
    private interface FilePredicate {
        boolean test(File file, String name, String suffix);
    }
}
