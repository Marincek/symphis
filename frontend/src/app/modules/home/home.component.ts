import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { TagInputModule } from 'ngx-chips';
import { Observable, of } from 'rxjs';
import { User, Link } from '@/shared/models';
import { LinkService } from '@/core/http';
import { AnalysisService } from '@/core/services';
import { AuthenticationService } from '@/core/authentication';

TagInputModule.withDefaults({
    tagInput: {
        placeholder: 'Add a new tag',
        // add here other default values for tag-input
    },
    dropdown: {
        displayBy: 'my-display-value',
        // add here other default values for tag-input-dropdown
    }
});

@Component({ 
    selector: 'app-root',
    styleUrls: ['home.component.css'],
    templateUrl: 'home.component.html' 
})
export class HomeComponent implements OnInit, OnDestroy {
    
    currentUser: User;
    currentUserSubscription: Subscription;
    links: Link[];
    linkurl: string;
    analysisTags: string[];
    linkTags: string[];

    public getTagsForLinkLoading = false;
    public getTagsForLinkButtonText: string = 'Get Tags';

    private tagAnalysisObservable : Observable<any[]> ; 

    constructor(private authenticationService: AuthenticationService, private linkService: LinkService, private analysisService: AnalysisService) {
        this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
            this.currentUser = user;
        });
    }

    ngOnInit() {
        this.linkurl = "https://www.androidcentral.com/samsung-galaxy-s10";
        this.linkTags = ["samsung","galaxy","sometag"];
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

    getTagsForLink() {
        this.getTagsForLinkLoading = true;
        this.getTagsForLinkButtonText = 'Loading ...';

        // this.analysisService.analyse(this.linkurl)
        //     .subscribe(data => { 
        //         this.analysisTags = data;
        //         this.getTagsForLinkLoading = false;
        //         this.getTagsForLinkButtonText = 'Get Tags';
        //     });

        
        var mocked = ["samplTag1", "samplTag2", "samplTag3"];
        return of(mocked).subscribe(data => {
            this.analysisTags = data;
            this.getTagsForLinkLoading = false;
            this.getTagsForLinkButtonText = 'Get Tags';
        });;
    }

    addLink(){
        var linkData = new Link();
        linkData.url = this.linkurl;
        linkData.tags = this.linkTags;

        this.linkService.add(linkData)
            .subscribe(data => {
                console.log('Link added '+ data.url)
            });;
    }

    private loadAllLinks() {
        this.linkService.getAll().pipe(first()).subscribe(links => {
            this.links = links;
        });
    }
}