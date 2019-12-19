package com.lzp.mybatis.parse;

/**
 * Created by LiZhanPing on 2019/12/19.
 */
public class SqlTokenParser {

    private static final String openToken = "#{";
    private static final String closeToken = "}";

    public static String parse(String sql) {
        if (sql == null || sql.isEmpty()) {
            return "";
        }
        int start = sql.indexOf(openToken);
        if (start == -1) {
            return sql;
        }
        char[] src = sql.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = sql.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = sql.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    builder.append("?");
                    offset = end + closeToken.length();
                }
            }
            start = sql.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String sql = "select" +
                " * " +
                "from " +
                "user " +
                "where id = #{id,jdbcType=INTEGER} and name = #{name,jdbcType=VARCHAR}";
        System.out.println(SqlTokenParser.parse(sql));
    }
}
