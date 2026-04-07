package com.libowen.fakecall.data.repository;

import com.libowen.fakecall.data.datastore.SettingsDataStore;
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
public final class SettingsRepositoryImpl_Factory implements Factory<SettingsRepositoryImpl> {
  private final Provider<SettingsDataStore> dataStoreProvider;

  private SettingsRepositoryImpl_Factory(Provider<SettingsDataStore> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public SettingsRepositoryImpl get() {
    return newInstance(dataStoreProvider.get());
  }

  public static SettingsRepositoryImpl_Factory create(
      Provider<SettingsDataStore> dataStoreProvider) {
    return new SettingsRepositoryImpl_Factory(dataStoreProvider);
  }

  public static SettingsRepositoryImpl newInstance(SettingsDataStore dataStore) {
    return new SettingsRepositoryImpl(dataStore);
  }
}
