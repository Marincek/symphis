import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User, Link } from '@/shared/models';
import { LinkService } from '@/core/http';
import { AnalysisService } from '@/core/services';
import { AuthenticationService } from '@/core/authentication';

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent implements OnInit, OnDestroy {
    currentUser: User;
    currentUserSubscription: Subscription;
    links: Link[];
    linkurl: string;
    tags: string[];

    private tagAnalysisObservable : Observable<any[]> ; 

    constructor(private authenticationService: AuthenticationService, private linkService: LinkService, private analysisService: AnalysisService) {
        this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
            this.currentUser = user;
        });
        this.linkurl = "https://www.androidcentral.com/samsung-galaxy-s10";
    }

    ngOnInit() {
        this.loadAllLinks();
    }

    ngOnDestroy() {
        this.currentUserSubscription.unsubscribe();
    }

    deleteLink(id: number) {
        this.linkService.delete(id).pipe(first()).subscribe(() => {
            this.loadAllLinks()
        });
    }

    analyseLink() {
        this.analysisService.analyse(this.linkurl)
            // .pipe(map(data => Array.from(data.values()))
            .subscribe(data => { 
                this.tags = data;
            });
    }

    private loadAllLinks() {
        this.linkService.getAll().pipe(first()).subscribe(links => {
            this.links = links;
        });
    }
}