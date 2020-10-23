package com.xie.lifeassistant.util;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.xie.lifeassistant.util.io.Attachment;
import com.xie.lifeassistant.util.io.AttachmentUtils;
import com.xie.lifeassistant.util.io.WebPathUtils;
import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    //以流的形式,将对象序列化后再反序列化,从而实现任意对象的深度克隆
    public static Object objectClone(Object obj){
        try (ByteOutputStream bos = new ByteOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            // 写入对象
            oos.writeObject(obj);
            oos.flush();
            // 使用ByteArrayInputStream和ObjectInputStream反序列化
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.getBytes()));) {
                // 读取对象
                return ois.readObject();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //驼峰与下划线字段的相互转换
    public static class LineAndHump {
        private static Pattern linePattern = Pattern.compile("_(\\w)");
        private static Pattern humpPattern = Pattern.compile("[A-Z]");
        /** 下划线转驼峰 */
        public static String lineToHump(String str) {
            str = str.toLowerCase();
            Matcher matcher = linePattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
        /** 驼峰转下划线*/
        public static String humpToLine(String str) {
            Matcher matcher = humpPattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
    }

    //LocalDateTime、LocalDate、Long、Date、String 相互转换
    public static class DataAndTime {
        public static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        public static Long getLong(Date date){
            return date.getTime();
        }
        public static Long getLong(LocalDate date){
            return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        public static Long getLong(LocalDateTime datetime){
            return datetime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }


        public static String getDateString(Long date){
            return getDateString(getLocalDate(date));
        }
        public static String getDateString(Timestamp date){
            return getDateString(getLocalDate(date));
        }
        public static String getDateString(LocalDate date){
            return date.format(df);
        }
        public static String getDateString(LocalDateTime date){
            return date.format(df);
        }

        public static String getDateTimeString(Long date){
            if(date == null){
                return "";
            }
            return getDateTimeString(getLocalDateTime(date));
        }
        public static String getDateTimeString(Timestamp date){
            if(date == null){
                return "";
            }
            return getDateTimeString(getLocalDateTime(date));
        }
        public static String getDateTimeString(LocalDateTime datetime){
            if(datetime == null){
                return "";
            }
            return datetime.format(dtf);
        }


        public static Date getDate(Long date){
            return new Date(date);
        }
        public static Date getDate(String date){
            return Date.from(LocalDateTime.parse(date,dtf).atZone(ZoneId.systemDefault()).toInstant());
        }
        public static Date getDate(LocalDate date){
            return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
        public static Date getDate(LocalDateTime datetime){
            return Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
        }

        public static Timestamp getTimestamp(){
            return getTimestamp(System.currentTimeMillis());
        }
        public static Timestamp getTimestamp(Long data){
            return getTimestamp(getLocalDateTime(data));
        }
        public static Timestamp getTimestamp(String data){
            if(data.length() >= 19){
                data = data.substring(0, 19);
                return getTimestamp(getLocalDateTime(data));
            }else if(data.length() >= 10){
                data = data.substring(0, 10);
                return getTimestamp(getLocalDate(data));
            }
            return null;
        }
        public static Timestamp getTimestamp(LocalDate data){
            return getTimestamp(getLocalDateTime(data));
        }
        public static Timestamp getTimestamp(LocalDateTime datetime){
            return Timestamp.valueOf(datetime);
        }

        public static LocalDate getLocalDate(Long date){
            return getLocalDate(getLocalDateTime(date));
        }
        public static LocalDate getLocalDate(String date){
            return LocalDate.parse(date,df);
        }
        public static LocalDate getLocalDate(Date date){
            return getLocalDate(getLocalDateTime(date));
        }
        public static LocalDate getLocalDate(Timestamp datetime){
            return getLocalDate(getLocalDateTime(datetime));
        }
        public static LocalDate getLocalDate(LocalDateTime datetime){
            return datetime.toLocalDate();
        }


        public static LocalDateTime getLocalDateTime(Long dateTime){
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.systemDefault());
        }
        public static LocalDateTime getLocalDateTime(String dateTime){
            return LocalDateTime.parse(dateTime,dtf);
        }
        public static LocalDateTime getLocalDateTime(Date date){
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
        public static LocalDateTime getLocalDateTime(Timestamp datetime){
            return datetime.toLocalDateTime();
        }
        public static LocalDateTime getLocalDateTime(LocalDate date){
            return LocalDateTime.of(date, LocalTime.parse("00:00:00"));
        }
    }


    //将Image、byte[]、Blob、BufferedImage、String、Attachment互相转换
    public static class BlobAndImage {

        public static Blob getBlob(BufferedImage image){
            return getBlob(image,"jpg");
        }

        public static Blob getBlob(BufferedImage image, String formatName){
            return new BlobImpl(getBytes(image, formatName));
        }

        //相对路径转blob
        public  static Blob getBlobByPath(String path){
            return getBlobByRealPath(AttachmentUtils.getAttachByUri(path).getPath().toString());
        }

        //绝对路径转blob
        public static Blob getBlobByRealPath(String absolutePath){
            return new BlobImpl(getBytesByAbsolutePath(absolutePath));
        }

        public static byte[] getBytes(Blob blob){
            try {
                return new BlobImpl(blob).getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public static byte[] getBytes(BufferedImage image){
            return getBytes(image,"jpg");
        }

        public static byte[] getBytes(BufferedImage image, String formatName){
            byte[] b = null;
            try{
                ByteArrayOutputStream bs =new ByteArrayOutputStream();
                ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(image,formatName,imOut); //scaledImage1为BufferedImage
                b=bs.toByteArray();
            }catch (Exception e){
                e.printStackTrace();
            }

            return b;
        }

        //相对路径转byte
        public  static byte[] getBytesByPath(String path){
            return getBytesByAbsolutePath(AttachmentUtils.getAttachByUri(path).getPath().toString());
        }

        //绝对路径转byte
        public static byte[] getBytesByAbsolutePath(String absolutePath){
            FileInputStream is = null;
            byte[] image = null;
            try {
                File file = new File(absolutePath);
//              System.out.println(file.toString());
//              File file2=new File(zplj.substring(0,zplj.lastIndexOf("."))+".jpg");
//
//              FileInputStream in=new FileInputStream(file);
//              FileOutputStream out=new FileOutputStream(file2);
//              int c;
//              byte buffer[]=new byte[1024];
//              while((c=in.read(buffer))!=-1){
//                  for(int i=0;i<c;i++)
//                      out.write(buffer[i]);
//              }
//              in.close();
//              out.close();

                is = new FileInputStream(file);
                int streamLength = (int)file.length();
                image = new byte[streamLength];
                is.read(image, 0, streamLength);//将字节流转换为byte数组
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                try {
                    //关闭输出流
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return image;
        }

        public static BufferedImage getImage(byte[] b){
            return getImage(new BlobImpl(b));
        }

        public static BufferedImage getImage(Blob blob){
            BufferedInputStream inputimage=null;
            BufferedImage image = null;
            try{
                try{
                    inputimage = new BufferedInputStream(blob.getBinaryStream());
                    image = ImageIO.read(inputimage);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(inputimage != null)
                        inputimage.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return image;
        }

        public static String getBase64(Blob blob) {
            String result = "";
            if(null != blob) {
                try {
                    InputStream msgContent = blob.getBinaryStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[100];
                    int n = 0;
                    while (-1 != (n = msgContent.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    result =new BASE64Encoder().encode(output.toByteArray()) ;
                    result = "data:image/jpeg;base64," + result;
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        //将文件转化为字节数组字符串，并对其进行Base64编码处理
        public static String getBase64(File file) {
            InputStream in = null;
            byte[] data = null;
            //读取图片字节数组

            try {
                in = new FileInputStream(file);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            //返回Base64编码过的字节数组字符串
            return encoder.encode(data);

        }

        //将base64字符转换为文件，并返回文件绝对路径
        public static String getFile(String fileName, String base64Code){
            FileOutputStream write =null;
            String tmpUri = "";
            try {
                String _tempPath = WebPathUtils.getNewTempRelativePath();
                String _tempRealPath = WebPathUtils.getAbsolutePathByRelativePath(_tempPath);
                Path _path = Paths.get(_tempRealPath);
                if(!Files.exists(_path)){
                    Files.createDirectories(_path);
                }

                int n =base64Code.indexOf(";base64,"); //data:image/jpeg;base64,aaaaaaaaaaaa
                String header =  base64Code.substring(0,n);
                String hz = header.substring(header.indexOf("/")+1);
                String code =base64Code.substring(n+8);


                if(StringUtils.isNotBlank(hz) && StringUtils.isNotBlank(code)){
                    Attachment attachment = AttachmentUtils.getAttachByRelativePath(_tempPath + fileName+"."+hz);
                    write = new FileOutputStream(attachment.getPath().toString());
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] decoderBytes = decoder.decodeBuffer(code);
                    write.write(decoderBytes);
                    write.flush();
                    tmpUri = attachment.getPath().toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }finally {
                if(write !=null){
                    try {
                        write.close();
                    }catch (Exception e){}
                }
            }
            return tmpUri;
        }

        public static Attachment getAtta(String fileName, String base64Code){
            return AttachmentUtils.getAttachByUri(getFile(fileName,base64Code));
        }

        public static class BlobImpl implements Blob {

            private byte[] _bytes = new byte[0];

            private int _length = 0;

            /**
             * 构造函数，以byte[]构建blob
             *
             * @param bytes
             */
            public BlobImpl(byte[] bytes) {
                init(bytes);
            }

            /**
             * 构造函数，以blob重新构建blob
             *
             * @param blob
             */
            public BlobImpl(Blob blob) {
                init(blobToBytes(blob));
            }

            /**
             * 初始化byte[]
             *
             * @param bytes
             */
            private void init(byte[] bytes) {
                _bytes = bytes;
                _length = _bytes.length;
            }

            /**
             * 将blob转为byte[]
             *
             * @param blob
             * @return
             */
            private byte[] blobToBytes(Blob blob) {
                try {
                    @Cleanup BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
                    byte[] bytes = new byte[(int) blob.length()];
                    int len = bytes.length;
                    int offset = 0;
                    int read = 0;
                    while (offset < len
                            && (read = is.read(bytes, offset, len - offset)) >= 0) {
                        offset += read;
                    }
                    return bytes;
                } catch (Exception e) {
                    return null;
                }
            }

            /**
             * 获得blob中数据实际长度
             *
             * @return
             * @throws SQLException
             */
            public long length() throws SQLException {
                return _bytes.length;
            }

            public byte[] getBytes() throws SQLException {
                return _bytes;
            }
            /**
             * 返回指定长度的byte[]
             *
             * @param pos
             * @param len
             * @return
             * @throws SQLException
             */
            public byte[] getBytes(long pos, int len) throws SQLException {
                if (pos == 0 && len == length())
                    return _bytes;
                try {
                    byte[] newbytes = new byte[len];
                    System.arraycopy(_bytes, (int) pos, newbytes, 0, len);
                    return newbytes;
                } catch (Exception e) {
                    throw new SQLException("Inoperable scope of this array");
                }
            }

            /**
             * 返回InputStream
             *
             * @return
             * @throws SQLException
             */
            public InputStream getBinaryStream() throws SQLException {
                return new ByteArrayInputStream(_bytes);
            }

            /**
             * 获取此byte[]中start的字节位置
             *
             * @param pattern
             * @param start
             * @return
             * @throws SQLException
             */
            public long position(byte[] pattern, long start) throws SQLException {
                start--;
                if (start < 0) {
                    throw new SQLException("start < 0");
                }
                if (start >= _length) {
                    throw new SQLException("start >= max length");
                }
                if (pattern == null) {
                    throw new SQLException("pattern == null");
                }
                if (pattern.length == 0 || _length == 0 || pattern.length > _length) {
                    return -1;
                }
                int limit = (int) _length - pattern.length;
                for (int i = (int) start; i <= limit; i++) {
                    int p;
                    for (p = 0; p < pattern.length && _bytes[i + p] == pattern[p]; p++) {
                        if (p == pattern.length) {
                            return i + 1;
                        }
                    }
                }
                return -1;
            }

            /**
             * 获取指定的blob中start的字节位置
             *
             * @param pattern
             * @param start
             * @return
             * @throws SQLException
             */
            public long position(Blob pattern, long start) throws SQLException {
                return position(blobToBytes(pattern), start);
            }

            /**
             * 不支持操作异常抛出
             *
             */
            void nonsupport() {
                throw new UnsupportedOperationException("This method is not supported！");
            }

            /**
             * 释放Blob对象资源
             *
             * @throws SQLException
             */
            public void free() throws SQLException {
                _bytes = new byte[0];
                _length = 0;
            }

            /**
             * 返回指定长度部分的InputStream，并返回InputStream
             *
             * @param pos
             * @param len
             * @return
             * @throws SQLException
             */
            public InputStream getBinaryStream(long pos, long len) throws SQLException {
                return new ByteArrayInputStream(getBytes(pos, (int) len));
            }

            /**
             * 以指定指定长度将二进制流写入OutputStream，并返回OutputStream
             *
             * @param pos
             * @return
             * @throws SQLException
             */
            public OutputStream setBinaryStream(long pos) throws SQLException {
                // 暂不支持
                nonsupport();
                pos--;
                if (pos < 0) {
                    throw new SQLException("pos < 0");
                }
                if (pos > _length) {
                    throw new SQLException("pos > length");
                }
                // 将byte[]转为ByteArrayInputStream
                ByteArrayInputStream inputStream = new ByteArrayInputStream(_bytes);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] bytes = new byte[(int) pos];
                try {
                    bytes = new byte[inputStream.available()];
                    int read;
                    while ((read = inputStream.read(bytes)) >= 0) {
                        os.write(bytes, 0, read);
                    }

                } catch (IOException e) {
                    e.getStackTrace();
                }

                // 返回OutputStream
                return (OutputStream) os;
            }

            /**
             * 设定byte[]
             *
             * @param pos
             * @param bytes
             * @param offset
             * @param size
             * @param copy
             * @return
             * @throws SQLException
             */
            private int setBytes(long pos, byte[] bytes, int offset, int size,
                                 boolean copy) throws SQLException {
                // 暂不支持
                nonsupport();
                pos--;
                if (pos < 0) {
                    throw new SQLException("pos < 0");
                }
                if (pos > _length) {
                    throw new SQLException("pos > max length");
                }
                if (bytes == null) {
                    throw new SQLException("bytes == null");
                }
                if (offset < 0 || offset > bytes.length) {
                    throw new SQLException("offset < 0 || offset > bytes.length");
                }
                if (size < 0 || pos + size > (long) Integer.MAX_VALUE
                        || offset + size > bytes.length) {
                    throw new SQLException();
                }
                // 当copy数据时
                if (copy) {
                    _bytes = new byte[size];
                    System.arraycopy(bytes, offset, _bytes, 0, size);
                } else { // 否则直接替换对象
                    _bytes = bytes;
                }
                return _bytes.length;
            }

            /**
             * 设定指定开始位置byte[]
             *
             * @param pos
             * @param bytes
             * @return
             * @throws SQLException
             */
            public int setBytes(long pos, byte[] bytes) throws SQLException {
                // 暂不支持
                nonsupport();
                return setBytes(pos, bytes, 0, bytes.length, true);
            }

            /**
             * 设定byte[]
             *
             * @param pos
             * @param bytes
             * @param offset
             * @param len
             * @return
             * @throws SQLException
             */
            public int setBytes(long pos, byte[] bytes, int offset, int len)
                    throws SQLException {
                // 暂不支持
                nonsupport();
                return setBytes(pos, bytes, offset, len, true);
            }

            /**
             * 截取相应部分数据
             *
             * @param len
             * @throws SQLException
             */
            public void truncate(long len) throws SQLException {
                if (len < 0) {
                    throw new SQLException("len < 0");
                }
                if (len > _length) {
                    throw new SQLException("len > max length");
                }
                _length = (int) len;
            }

            //public static void main(String[] args) {
            //    // 获得一个指定文件的blob
            //    // PS:天地良心啊，没抄袭hibernate写法，无奈的写一样了，不过比他多实现了点方法……（还特意把函数改名，不然更像|||）……
            //    Blob blob = Loon.makeBlob("D:\test.txt");
            //    // 以byte[]获得blob实例
            //    // Blob blob = new BlobImpl(bytes);
            //    try {
            //        // getBytes测试
            //        // 取出0到blob结束范围的byte[]
            //        byte[] buffer = blob.getBytes(0, (int) blob.length());
            //        // 以gb2312编码将byte[]转为string显示
            //        System.out.println(new String(buffer, "gb2312"));
            //
            //        // getBinaryStream测试
            //        BufferedInputStream is = new BufferedInputStream(
            //                blob.getBinaryStream());
            //        buffer = new byte[(int) blob.length()];
            //        int len = buffer.length;
            //        int offset = 0;
            //        int read = 0;
            //        while (offset < len
            //                && (read = is.read(buffer, offset, len - offset)) >= 0) {
            //            offset += read;
            //        }
            //        System.out.println(new String(buffer, "gb2312"));
            //
            //        // getBinaryStream范围取值测试,取0to4的byte[]
            //        is = new BufferedInputStream(blob.getBinaryStream(0, 4));
            //        // 将is转为byte[]后转为String显示
            //        System.out.println(FileHelper.readToString(is, "gb2312"));
            //
            //    } catch (Exception e) {
            //        e.printStackTrace();
            //    }
            //
            //}

        }

    }
}
