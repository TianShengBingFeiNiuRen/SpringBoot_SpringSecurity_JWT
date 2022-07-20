package com.nonce.restsecurity.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author Andon
 * 2022/5/16
 */
@Slf4j
public class FileUtil {

    private static Path rootFilePath;

    static {
        try {
//            String osName = System.getProperty("os.name");
//            log.info("osName:{}", osName);
//            String fileRootPath; //文件存放目录
//            if (osName.toLowerCase().contains("windows")) {
//                fileRootPath = "D:\\apps\\file"; // 指定windows系统下RocksDB文件目录
//            } else {
//                fileRootPath = "/usr/local/apps/file"; // 指定linux系统下RocksDB文件目录
//            }
//            rootFilePath = Paths.get(fileRootPath);
            rootFilePath = Paths.get(System.getProperty("user.dir"));
            if (!Files.exists(rootFilePath)) {
                Files.createDirectories(rootFilePath);
            }
            log.info("rootFilePath:{}", rootFilePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("FileUtil init failure!! error:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取绝对路径 Path
     *
     * @param fileNamePath 文件名称路径
     */
    public static Path getRootFilePath(String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }

    /**
     * 创建文件
     *
     * @param fileNamePath 文件名称路径
     */
    public static File createFile(String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.createFile(path);
        log.info("createFile success!! --文件-> [{}]", path.toAbsolutePath().toString());
        return path.toFile();
    }

    /**
     * 保存文件
     *
     * @param multipartFile multipartFile
     * @param fileNamePath  文件名称路径
     */
    public static String save(MultipartFile multipartFile, String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.copy(multipartFile.getInputStream(), path);
        log.info("save success!! --文件-> [{}]", path.toAbsolutePath().toString());
        return path.toAbsolutePath().toString();
    }

    /**
     * 复制文件
     *
     * @param fromFilePath 源文件绝对路径
     * @param fileNamePath 目标文件名称路径
     */
    public static String copy(String fromFilePath, String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.copy(new File(fromFilePath).toPath(), path);
        log.info("copy success!! --文件-> [{}]", path.toAbsolutePath().toString());
        return path.toAbsolutePath().toString();
    }

    /**
     * 根据内容创建文件
     *
     * @param lines    文件内容
     * @param fileName 文件名称
     */
    public static File createFileWithContent(List<String> lines, String... fileName) throws IOException {
        File file = createFile(fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            log.info("createFileWithContent success!! --文件-> [{}]", file.getAbsolutePath());
            return file;
        }
    }

    /**
     * 增量写文件
     *
     * @param file  目标文件对象
     * @param lines 文件内容
     */
    public static void appendContentToFile(File file, List<String> lines) throws IOException {
        synchronized (FileUtil.class) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
        }
    }

    /**
     * 读大文件
     *
     * @param filePath      文件绝对路径
     * @param contentReader contentReader
     */
    public static void readFileContentByLine(String filePath, Consumer<BufferedReader> contentReader) throws Exception {
        String charset = charset(filePath);
        try (FileInputStream inputStream = new FileInputStream(filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            contentReader.accept(bufferedReader);
        }
    }

    /**
     * 读文件首行
     *
     * @param filePath 文件绝对路径
     */
    public static String readFirstLine(String filePath) throws Exception {
        AtomicReference<String> firstLine = new AtomicReference<>("");
        readFileContentByLine(filePath, bufferedReader -> {
            try {
                firstLine.set(bufferedReader.readLine());
            } catch (Exception e) {
                firstLine.set("");
            }
        });
        log.info("readFirstLine success!! --文件-> [{}] schema={}", filePath, firstLine);
        return firstLine.get();
    }

    /**
     * 读取文件行数
     *
     * @param filePath 文件绝对路径
     */
    public static int readDataCount(String filePath) throws Exception {
        int linesCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                linesCount++;
            }
        }
        log.info("readDataCount success!! --文件-> [{}] linesCount={}", filePath, linesCount);
        return linesCount;
    }

    /**
     * 判断文件字符编码
     */
    public static String charset(String path) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 > read || read > 0xBF) {
                            break; // 双字节 (0xC0 - 0xDF),(0x80 - 0xBF),也可能在GB编码内
                        }
                    } else if (0xE0 <= read) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                            }
                        }
                        break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("--文件-> [{}] 采用的字符集为: [{}]", path, charset);
        return charset;
    }
}
