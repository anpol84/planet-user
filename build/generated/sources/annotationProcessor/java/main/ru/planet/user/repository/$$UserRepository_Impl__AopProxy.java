package ru.planet.user.repository;

import jakarta.annotation.Nullable;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.dto.GetUser;
import ru.tinkoff.kora.common.AopProxy;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultSetMapper;
import ru.tinkoff.kora.logging.common.arg.StructuredArgument;
import ru.tinkoff.kora.logging.common.arg.StructuredArgumentMapper;

@AopProxy
@Generated({"ru.tinkoff.kora.aop.annotation.processor.AopAnnotationProcessor", "ru.tinkoff.kora.logging.aspect.LogAspect"})
public final class $$UserRepository_Impl__AopProxy extends $UserRepository_Impl {
  private final ILoggerFactory iLoggerFactory1;

  private final StructuredArgumentMapper<Long> structuredArgumentMapper1;

  private final StructuredArgumentMapper<List<String>> structuredArgumentMapper2;

  private final StructuredArgumentMapper<CreateUser> structuredArgumentMapper3;

  private final StructuredArgumentMapper<GetUser> structuredArgumentMapper4;

  private final StructuredArgumentMapper<UpdateCount> structuredArgumentMapper5;

  private final Logger logger1;

  private final Logger logger2;

  private final Logger logger3;

  private final Logger logger4;

  private final Logger logger5;

  private final Logger logger6;

  private final Logger logger7;

  public $$UserRepository_Impl__AopProxy(JdbcConnectionFactory _connectionFactory,
      JdbcResultSetMapper<List<String>> _result_mapper_1,
      JdbcResultSetMapper<Long> _result_mapper_2, JdbcResultSetMapper<GetUser> _result_mapper_3,
      ILoggerFactory iLoggerFactory1,
      @Nullable StructuredArgumentMapper<Long> structuredArgumentMapper1,
      @Nullable StructuredArgumentMapper<List<String>> structuredArgumentMapper2,
      @Nullable StructuredArgumentMapper<CreateUser> structuredArgumentMapper3,
      @Nullable StructuredArgumentMapper<GetUser> structuredArgumentMapper4,
      @Nullable StructuredArgumentMapper<UpdateCount> structuredArgumentMapper5) {
    super(_connectionFactory, _result_mapper_1, _result_mapper_2, _result_mapper_3);
    this.iLoggerFactory1 = iLoggerFactory1;
    this.structuredArgumentMapper1 = structuredArgumentMapper1;
    this.structuredArgumentMapper2 = structuredArgumentMapper2;
    this.structuredArgumentMapper3 = structuredArgumentMapper3;
    this.structuredArgumentMapper4 = structuredArgumentMapper4;
    this.structuredArgumentMapper5 = structuredArgumentMapper5;
    this.logger1 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.findRoles");
    this.logger2 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.insertUser");
    this.logger3 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.insertUserRole");
    this.logger4 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.getUser");
    this.logger5 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.updateUser");
    this.logger6 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.deleteUser");
    this.logger7 = iLoggerFactory1.getLogger("ru.planet.user.repository.$UserRepository_Impl.deleteUserRoles");
  }

  private List<String> _findRoles_AopProxy_LogAspect(Long id) {
    if (logger1.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger1.info(__dataIn, ">");
    } else {
      logger1.info(">");
    }

    try {
      var __result = super.findRoles(id);
      if (logger1.isDebugEnabled()) {
        var __dataOut = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          if (this.structuredArgumentMapper2 != null) {
            gen.writeFieldName("out");
            this.structuredArgumentMapper2.write(gen, __result);
          } else {
            gen.writeStringField("out", String.valueOf(__result));
          }
          gen.writeEndObject();
        }
        );
        logger1.info(__dataOut, "<");
      } else {
        logger1.info("<");
      }
      return __result;
    } catch(Throwable __error) {
      if (logger1.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger1.isDebugEnabled()) {
          logger1.warn(__dataError, "<", __error);
        } else {
          logger1.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public List<String> findRoles(Long id) {
    return this._findRoles_AopProxy_LogAspect(id);
  }

  private long _insertUser_AopProxy_LogAspect(CreateUser user) {
    if (logger2.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper3 != null) {
          gen.writeFieldName("user");
          this.structuredArgumentMapper3.write(gen, user);
        } else {
          gen.writeStringField("user", String.valueOf(user));
        }
        gen.writeEndObject();
      } );
      logger2.info(__dataIn, ">");
    } else {
      logger2.info(">");
    }

    try {
      var __result = super.insertUser(user);
      if (logger2.isDebugEnabled()) {
        var __dataOut = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          if (this.structuredArgumentMapper1 != null) {
            gen.writeFieldName("out");
            this.structuredArgumentMapper1.write(gen, __result);
          } else {
            gen.writeStringField("out", String.valueOf(__result));
          }
          gen.writeEndObject();
        }
        );
        logger2.info(__dataOut, "<");
      } else {
        logger2.info("<");
      }
      return __result;
    } catch(Throwable __error) {
      if (logger2.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger2.isDebugEnabled()) {
          logger2.warn(__dataError, "<", __error);
        } else {
          logger2.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public long insertUser(CreateUser user) {
    return this._insertUser_AopProxy_LogAspect(user);
  }

  private void _insertUserRole_AopProxy_LogAspect(Long id) {
    if (logger3.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger3.info(__dataIn, ">");
    } else {
      logger3.info(">");
    }

    try {
      super.insertUserRole(id);
      logger3.info("<");
    } catch(Throwable __error) {
      if (logger3.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger3.isDebugEnabled()) {
          logger3.warn(__dataError, "<", __error);
        } else {
          logger3.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public void insertUserRole(Long id) {
    this._insertUserRole_AopProxy_LogAspect(id);
  }

  private GetUser _getUser_AopProxy_LogAspect(Long id) {
    if (logger4.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger4.info(__dataIn, ">");
    } else {
      logger4.info(">");
    }

    try {
      var __result = super.getUser(id);
      if (logger4.isDebugEnabled()) {
        var __dataOut = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          if (this.structuredArgumentMapper4 != null) {
            gen.writeFieldName("out");
            this.structuredArgumentMapper4.write(gen, __result);
          } else {
            gen.writeStringField("out", String.valueOf(__result));
          }
          gen.writeEndObject();
        }
        );
        logger4.info(__dataOut, "<");
      } else {
        logger4.info("<");
      }
      return __result;
    } catch(Throwable __error) {
      if (logger4.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger4.isDebugEnabled()) {
          logger4.warn(__dataError, "<", __error);
        } else {
          logger4.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public GetUser getUser(Long id) {
    return this._getUser_AopProxy_LogAspect(id);
  }

  private UpdateCount _updateUser_AopProxy_LogAspect(CreateUser user, Long id) {
    if (logger5.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper3 != null) {
          gen.writeFieldName("user");
          this.structuredArgumentMapper3.write(gen, user);
        } else {
          gen.writeStringField("user", String.valueOf(user));
        }
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger5.info(__dataIn, ">");
    } else {
      logger5.info(">");
    }

    try {
      var __result = super.updateUser(user, id);
      if (logger5.isDebugEnabled()) {
        var __dataOut = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          if (this.structuredArgumentMapper5 != null) {
            gen.writeFieldName("out");
            this.structuredArgumentMapper5.write(gen, __result);
          } else {
            gen.writeStringField("out", String.valueOf(__result));
          }
          gen.writeEndObject();
        }
        );
        logger5.info(__dataOut, "<");
      } else {
        logger5.info("<");
      }
      return __result;
    } catch(Throwable __error) {
      if (logger5.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger5.isDebugEnabled()) {
          logger5.warn(__dataError, "<", __error);
        } else {
          logger5.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public UpdateCount updateUser(CreateUser user, Long id) {
    return this._updateUser_AopProxy_LogAspect(user, id);
  }

  private UpdateCount _deleteUser_AopProxy_LogAspect(Long id) {
    if (logger6.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger6.info(__dataIn, ">");
    } else {
      logger6.info(">");
    }

    try {
      var __result = super.deleteUser(id);
      if (logger6.isDebugEnabled()) {
        var __dataOut = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          if (this.structuredArgumentMapper5 != null) {
            gen.writeFieldName("out");
            this.structuredArgumentMapper5.write(gen, __result);
          } else {
            gen.writeStringField("out", String.valueOf(__result));
          }
          gen.writeEndObject();
        }
        );
        logger6.info(__dataOut, "<");
      } else {
        logger6.info("<");
      }
      return __result;
    } catch(Throwable __error) {
      if (logger6.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger6.isDebugEnabled()) {
          logger6.warn(__dataError, "<", __error);
        } else {
          logger6.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public UpdateCount deleteUser(Long id) {
    return this._deleteUser_AopProxy_LogAspect(id);
  }

  private void _deleteUserRoles_AopProxy_LogAspect(Long id) {
    if (logger7.isDebugEnabled()) {
      var __dataIn = StructuredArgument.marker("data", gen ->  {
        gen.writeStartObject();
        if (this.structuredArgumentMapper1 != null) {
          gen.writeFieldName("id");
          this.structuredArgumentMapper1.write(gen, id);
        } else {
          gen.writeStringField("id", String.valueOf(id));
        }
        gen.writeEndObject();
      } );
      logger7.info(__dataIn, ">");
    } else {
      logger7.info(">");
    }

    try {
      super.deleteUserRoles(id);
      logger7.info("<");
    } catch(Throwable __error) {
      if (logger7.isWarnEnabled()) {
        var __dataError = StructuredArgument.marker("data", gen -> {
          gen.writeStartObject();
          gen.writeStringField("errorType", __error.getClass().getCanonicalName());
          gen.writeStringField("errorMessage", __error.getMessage());
          gen.writeEndObject();
        } );

        if(logger7.isDebugEnabled()) {
          logger7.warn(__dataError, "<", __error);
        } else {
          logger7.warn(__dataError, "<");
        }
      }
      throw __error;
    }
  }

  @Override
  public void deleteUserRoles(Long id) {
    this._deleteUserRoles_AopProxy_LogAspect(id);
  }
}
