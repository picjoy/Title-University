package com.seven.codesnippet.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsePostsDto<T> {
  private boolean success;
  private T posts;
  private Error error;

  public static <T> ResponsePostsDto<T> success(T data) {
    return new ResponsePostsDto<>(true, data, null);
  }

  public static <T> ResponsePostsDto<T> fail(String code, String message) {
    return new ResponsePostsDto<>(false, null, new Error(code, message));
  }

  @Getter
  @AllArgsConstructor
  static class Error {
    private String code;
    private String message;
  }

}
