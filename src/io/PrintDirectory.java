package io;

import java.io.File;

/**
 * @author baB_hyf
 * @date 2022/08/01
 */
public class PrintDirectory {

    public static void main(String[] args) {
        print("F:\\storage\\rocketmq\\store\\debug\\");
    }

    public static void print(String directoryPath) {
        print(directoryPath, false);
    }

    public static void print(String directoryPath, boolean includeThis) {
        File dir = new File(directoryPath);
        StringBuilder sb = new StringBuilder();

        if (includeThis) {
            print(dir, 0, sb);
        }
        else {
            print(dir, -1, sb);
        }

        System.out.println(sb.toString());
    }

    private static void print(File dir, int hierarchy, StringBuilder sb) {
        fillHierarchyString(sb, hierarchy);
        if (hierarchy != -1) {
            sb.append(dir.getName());
            if (dir.isDirectory()) {
                sb.append('/');
            }
            sb.append("\n");
        }
        else {
            hierarchy = 0;
        }

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    print(file, hierarchy + 1, sb);
                }
            }
        }
    }

    private static void fillHierarchyString(StringBuilder sb, int hierarchy) {
        if (hierarchy > 0) {
            for (int i = 0; i < hierarchy - 1; i++) {
                sb.append("    ");
            }
            sb.append("|__");
        }
    }
}
