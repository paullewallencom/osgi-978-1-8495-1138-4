package com.packtpub.felix.bookshelf.service.impl;

public interface LoggerConstants
{
    String LOG_EDIT_BY_ISBN = "LOG_EDIT_BY_ISBN: Get book for edit: [isbn={0}]";

    String LOG_GET_BY_ISBN = "LOG_GET_BY_ISBN: Get book for read: [isbn={0}]";

    String LOG_ADD_BOOK =
        "LOG_ADD_BOOK: Add book: [isbn={0}] [title={1}] [author={2}] [category={3}] [rating={4}]";

    String LOG_CREATE_BOOK = "LOG_CREATE_BOOK: Create new book [isbn={0}]";

    String LOG_STORE_BOOK = "LOG_STORE_BOOK: Store book [isbn={0}]";

    String LOG_CHECK_SESSION_FAILED =
        "LOG_CHECK_SESSION_FAILED: Session check failed, [current={0}] [requested={1}]";

    String LOG_CHECK_SESSION_SUCCESS = "LOG_CHECK_SESSION_SUCCESS: Session check succeeded [session={0}]";

    String LOG_LOGIN_ATTEMPT = "LOG_LOGIN_ATTEMPT: Login attempt [user={0}]";

    String LOG_LOGIN_SUCCESS = "LOG_LOGIN_SUCCESS: Login success [user={0}] [session={1}]";

    String LOG_LOGOUT_SESSION = "LOG_LOGOUT_SESSION: Logout [session={0}]";

    String LOG_MODIFY_CATEGORY = "LOG_MODIFY_CATEGORY: Modify book category [isbn={0}] [category={1}]";

    String LOG_MODIFY_RATING = "LOG_MODIFY_RATING: Modify book rating [isbn={0}] [rating={1}]";

    String LOG_GET_CATEGORIES = "LOG_GET_CATEGORIES: Get all categories";

    String LOG_REMOVE_BOOK = "LOG_REMOVE_BOOK: Remove book [isbn={0}]";

    String LOG_SEARCH_BY_AUTHOR = "LOG_SEARCH_BY_AUTHOR: Searching by author [like={0}]";

    String LOG_SEARCH_BY_CATEGORY = "LOG_SEARCH_BY_CATEGORY: Searching by category [like={0}]";

    String LOG_SEARCH_BY_TITLE = "LOG_SEARCH_BY_TITLE: Searching by title [like={0}]";

    String LOG_SEARCH_BY_RATING = "LOG_SEARCH_BY_RATING: Searching by rating between [lower={0}] and [upper={1}]";

}
