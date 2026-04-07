package com.libowen.fakecall.ui.contacts;

import com.libowen.fakecall.domain.repository.CallerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<CallerRepository> repositoryProvider;

  private ContactsViewModel_Factory(Provider<CallerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ContactsViewModel_Factory create(Provider<CallerRepository> repositoryProvider) {
    return new ContactsViewModel_Factory(repositoryProvider);
  }

  public static ContactsViewModel newInstance(CallerRepository repository) {
    return new ContactsViewModel(repository);
  }
}
