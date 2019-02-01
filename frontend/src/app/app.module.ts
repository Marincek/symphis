import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { TagInputModule } from 'ngx-chips';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // this is needed for tag lib
import { ReactiveFormsModule }    from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';
import { FormsModule } from '@angular/forms';

import { AlertComponent } from './shared/components/alert';
import { TokenInterceptor, ErrorInterceptor } from './core/interceptors';
import { HomeComponent } from './modules/home';
import { LoginComponent } from './modules/login';
import { RegisterComponent } from './modules/register';

// used to create fake backend
import { FakeBackendInterceptor } from './core/interceptors';

@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        routing,
        FormsModule,
        TagInputModule, 
        BrowserAnimationsModule
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        // provider used to create fake backend
        { provide: HTTP_INTERCEPTORS, useClass: FakeBackendInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }