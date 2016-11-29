(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('corps-etat', {
            parent: 'entity',
            url: '/corps-etat?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.corpsEtat.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/corps-etat/corps-etats.html',
                    controller: 'CorpsEtatController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('corpsEtat');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('corps-etat-detail', {
            parent: 'entity',
            url: '/corps-etat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.corpsEtat.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/corps-etat/corps-etat-detail.html',
                    controller: 'CorpsEtatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('corpsEtat');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CorpsEtat', function($stateParams, CorpsEtat) {
                    return CorpsEtat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'corps-etat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('corps-etat-detail.edit', {
            parent: 'corps-etat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corps-etat/corps-etat-dialog.html',
                    controller: 'CorpsEtatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CorpsEtat', function(CorpsEtat) {
                            return CorpsEtat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('corps-etat.new', {
            parent: 'corps-etat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corps-etat/corps-etat-dialog.html',
                    controller: 'CorpsEtatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('corps-etat', null, { reload: 'corps-etat' });
                }, function() {
                    $state.go('corps-etat');
                });
            }]
        })
        .state('corps-etat.edit', {
            parent: 'corps-etat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corps-etat/corps-etat-dialog.html',
                    controller: 'CorpsEtatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CorpsEtat', function(CorpsEtat) {
                            return CorpsEtat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('corps-etat', null, { reload: 'corps-etat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('corps-etat.delete', {
            parent: 'corps-etat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corps-etat/corps-etat-delete-dialog.html',
                    controller: 'CorpsEtatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CorpsEtat', function(CorpsEtat) {
                            return CorpsEtat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('corps-etat', null, { reload: 'corps-etat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
