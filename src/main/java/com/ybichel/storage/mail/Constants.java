package com.ybichel.storage.mail;

public final class Constants {
    private Constants() {}

    public static final String IMAGE_ROW_TEMPLATE =
            "<tr>" +
                "<td align=\"center\">" +
                "</td>" +
            "</tr>";

    public static String getGreetingTemplate(String firstName, String greetingMessage) {
        return "<tr>" +
                    "<td align=\"center\">" +
                        "<p>Dear " + firstName + ",</p>" +
                        "<p>" + greetingMessage + "</p>" +
                    "</td>" +
                "</tr>";
    }

    public static String getButtonLink(String url, String label) {
        return
                "<tr>" +
                    "<td align=\"center\">" +
                        "<table cellspacing=\"0\" cellpadding=\"0\">" +
                            "<tr>" +
                                "<td style=\"border-radius: 2px;\" bgcolor=\"#57A6BF\">" +
                                    "<a href=\"" + url + "\" target=\"_blank\" style=\"padding: 8px 12px; border: 1px solid #57A6BF;border-radius: 2px;font-family: Helvetica, Arial, sans-serif;font-size: 14px; color: #ffffff;text-decoration: none;font-weight:bold;display: inline-block;\">" +
                                        label +
                                    "</a>"+
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</td>" +
                "</tr>";
    }

    public static final String EMAIL_SIGNATURE =
            "<table style=\"font-size:12px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
                "<tr>" +
                    "<td align=\"center\">" +
                        "<br/><b>MoleCare Team</b><br/>" +
                    "</td>" +
                "</tr>" +
                "<tr>" +
                    "<td>" +
                        "<br/>Find more about what we do on <a href=\"http://www.molecare.co.uk\" target=\"_blank\">http://www.molecare.co.uk</a>." +
                    "</td>" +
                "</tr>" +
            "</table>";

    public static String getEmailFooter(String userEmail) {
        return "<footer style='background=#E4D8D6; margin=\"0 auto\"'>" +
                    "<table style=\"font-size:12px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
                        "<tr>" +
                            "<td align=\"center\">" +
                            "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>" +
                                "<p>" +
                                    "This message was sent to " + userEmail + ". If you have questions or complaints, please contact us at <a href='mailto:info@2ay.co.uk' target='_blank'>info@2ay.co.uk</a>." +
                                "</p>" +
                            "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>" +
                                "<p>2AY Ltd, 71-75 Shelton Street London WC2H 9HQ</p>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</footer>";
    }
}
