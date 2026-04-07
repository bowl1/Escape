package com.libowen.fakecall.system.di;

import com.libowen.fakecall.data.db.AppDatabase;
import com.libowen.fakecall.data.db.dao.CallerDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class DatabaseModule_ProvideCallerDaoFactory implements Factory<CallerDao> {
  private final Provider<AppDatabase> dbProvider;

  private DatabaseModule_ProvideCallerDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CallerDao get() {
    return provideCallerDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCallerDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideCallerDaoFactory(dbProvider);
  }

  public static CallerDao provideCallerDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCallerDao(db));
  }
}
