package com.ragm_sales.model;

import java.io.Serializable;

public class UserModel extends StatusResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private User user;
        private String access_token;
        private String token_type;
        private String expires_in;

        public User getUser() {
            return user;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }


        public class User implements Serializable{
            private int id;
            private String name;
            private String user_name;
            private String email_verified_at;
            private String photo;
            private String lang;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getEmail_verified_at() {
                return email_verified_at;
            }

            public String getPhoto() {
                return photo;
            }

            public String getLang() {
                return lang;
            }
        }
    }

}
