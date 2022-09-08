import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'to-do-item',
        data: { pageTitle: 'toDoApp.toDoItem.home.title' },
        loadChildren: () => import('./to-do-item/to-do-item.module').then(m => m.ToDoItemModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'toDoApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
