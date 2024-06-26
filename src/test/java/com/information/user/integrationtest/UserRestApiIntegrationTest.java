package com.information.user.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 全てのユーザーが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                           {
                             "id": 1,
                             "name": "suzuki",
                             "birthdate": "1973/07/22"
                           },
                           {
                             "id": 2,
                             "name": "kato",
                             "birthdate": "1960/03/15"
                           },
                           {
                             "id": 3,
                             "name": "tanaka",
                             "birthdate": "1991/12/08"
                           }
                        ]
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 存在するユーザーのIDを指定したときにユーザーを取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                           {
                             "id": 1,
                             "name": "suzuki",
                             "birthdate": "1973/07/22"
                           }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 存在しないユーザーのIDを指定したときに404エラーを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/0"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("user not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/users/0"));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/insertUsers.yml", ignoreCols = "id")
    @Transactional
    void 新しいユーザーを作成しそのユーザーに固有のIDを割り当てて登録できること() throws Exception {
        String newUser = """
                       {
                         "name": "hayashi",
                         "birthdate": "1992/03/09"
                       }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUser))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                           {
                             "message": "user created"
                           }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 空のリクエストボディを送信して新しいユーザーを登録しようとした場合に400エラーを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/updateUsers.yml")
    @Transactional
    void ユーザーIDを指定して更新するAPIが正常に動作すること() throws Exception {
        String updateUser = """
                    {
                      "name": "sato",
                      "birthdate": "1988/04/18"
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUser))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                           {
                             "message": "user updated"
                           }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 存在しないユーザーIDを指定して更新したときに404エラーを返すこと() throws Exception {
        String updateUser = """
                      {
                        "name": "sato",
                        "birthdate": "1988/04/18"
                      }
                """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUser))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("user not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/users/0"));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 空のリクエストボディを送信してユーザーを更新しようとした場合に400エラーを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/deleteUsers.yml")
    @Transactional
    void ユーザーIDを指定して削除するAPIが正常に動作すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                           {
                             "message": "user deleted"
                           }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 存在しないユーザーIDを指定して削除したときに404エラーを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/0"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("user not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/users/0"));
    }
}
