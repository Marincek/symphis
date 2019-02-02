import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { TagInputModule } from 'ngx-chips';
import { User, Link } from '@/shared/models';
import { LinkService } from '@/core/http';
import { AnalysisService } from '@/core/services';
import { AuthenticationService } from '@/core/authentication';
import { AlertService } from '@/core/services';

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
    linkTags: string[] = [];
    linkTagsSuggested: string[];

    public getTagsForLinkLoading = false;
    public getTagsForLinkButtonText: string = 'Get Tags';

    constructor(
        private authenticationService: AuthenticationService,
        private linkService: LinkService,
        private analysisService: AnalysisService,
        private alertService: AlertService) {

        this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
            this.currentUser = user;
        });
    }

    ngOnInit() {
        this.linkurl = "https://www.simplyrecipes.com/recipes/sun_dried_tomato_pesto/";
        this.loadAllLinks();
    }

    ngOnDestroy() {
        this.currentUserSubscription.unsubscribe();
    }

    deleteLink(id: number) {
        this.linkService.delete(id).pipe(first())
            .subscribe(() => {
                this.links = this.links.filter(function (value, index, arr) {
                    return value.id != id;
                })
            });
    }

    getTagsForLink() {
        this.getTagsForLinkLoading = true;
        this.getTagsForLinkButtonText = 'Loading ...';

        this.analysisService.analyse(this.linkurl)
            .subscribe(
                data => {
                    this.linkTagsSuggested = data as string[];
                    this.getTagsForLinkLoading = false;
                    this.getTagsForLinkButtonText = 'Get Tags';
                },
                error => {
                    this.alertService.error(error);
                });;
    }

    appendSuggetedTag($event: string) {
        this.linkTags.push($event);
    }

    addLink() {
        var linkData = new Link();
        linkData.url = this.linkurl;
        linkData.tags = this.linkTags;

        this.linkService.add(linkData)
            .subscribe(
                resLink => {
                    console.log('Link added ' + resLink.url)
                    this.links.push(resLink);
                    //reset tags
                    this.linkTagsSuggested = null;
                    this.linkTags = [];
                    this.linkurl = "";
                },
                error => {
                    this.alertService.error(error);
                });;
    }

    private loadAllLinks() {
        this.linkService.getAll()
            .subscribe(
                links => {
                    this.links = links;
                },
                error => {
                    this.alertService.error(error);
                });
    }
}