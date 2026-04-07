package com.libowen.fakecall.data.repository;

import com.libowen.fakecall.data.db.dao.CallerDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class CallerRepositoryImpl_Factory implements Factory<CallerRepositoryImpl> {
  private final Provider<CallerDao> daoProvider;

  private CallerRepositoryImpl_Factory(Provider<CallerDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public CallerRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static CallerRepositoryImpl_Factory create(Provider<CallerDao> daoProvider) {
    return new CallerRepositoryImpl_Factory(daoProvider);
  }

  public static CallerRepositoryImpl newInstance(CallerDao dao) {
    return new CallerRepositoryImpl(dao);
  }
}
