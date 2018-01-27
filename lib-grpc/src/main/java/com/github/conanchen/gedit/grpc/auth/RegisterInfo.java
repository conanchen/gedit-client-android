package com.github.conanchen.gedit.grpc.auth;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.user.auth.grpc.Question;

import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterInfo {
    public String token;
    public String mobile;
    public String smscode;
    public String questionUuid;
    public String questionTip;
    public List<Question> question;
    public String password;
    public Status status;

    public RegisterInfo(String token, String mobile, String smscode, String questionUuid, String questionTip, List<Question> question,
                        String password, Status status) {
        this.token = token;
        this.mobile = mobile;
        this.smscode = smscode;
        this.questionUuid = questionUuid;
        this.questionTip = questionTip;
        this.question = question;
        this.password = password;
        this.status = status;
    }

    public static RegisterInfo.Builder builder() {
        return new RegisterInfo.Builder();
    }

    public static final class Builder {
        private String token;
        private String mobile;
        private String smscode;
        private String questionUuid;
        private String questionTip;
        private List<Question> question;
        private String password;
        private Status status;

        public Builder() {
        }

        public RegisterInfo build() {
            return new RegisterInfo(token, mobile, smscode, questionUuid, questionTip, question, password, status);
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder setSmscode(String smscode) {
            this.smscode = smscode;
            return this;
        }

        public Builder setQuestionUuid(String questionUuid) {
            this.questionUuid = questionUuid;
            return this;
        }

        public Builder setQuestionTip(String questionTip) {
            this.questionTip = questionTip;
            return this;
        }

        public Builder setQuestion(List<Question> question) {
            this.question = question;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }
    }
}
