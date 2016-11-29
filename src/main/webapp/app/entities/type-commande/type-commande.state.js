(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-commande', {
            parent: 'entity',
            url: '/type-commande?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.typeCommande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-commande/type-commandes.html',
                    controller: 'TypeCommandeController',
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
                    $translatePartialLoader.addPart('typeCommande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-commande-detail', {
            parent: 'entity',
            url: '/type-commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.typeCommande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-commande/type-commande-detail.html',
                    controller: 'TypeCommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeCommande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeCommande', function($stateParams, TypeCommande) {
                    return TypeCommande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-commande-detail.edit', {
            parent: 'type-commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-commande/type-commande-dialog.html',
                    controller: 'TypeCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeCommande', function(TypeCommande) {
                            return TypeCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-commande.new', {
            parent: 'type-commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-commande/type-commande-dialog.html',
                    controller: 'TypeCommandeDialogController',
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
                    $state.go('type-commande', null, { reload: 'type-commande' });
                }, function() {
                    $state.go('type-commande');
                });
            }]
        })
        .state('type-commande.edit', {
            parent: 'type-commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-commande/type-commande-dialog.html',
                    controller: 'TypeCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeCommande', function(TypeCommande) {
                            return TypeCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-commande', null, { reload: 'type-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-commande.delete', {
            parent: 'type-commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-commande/type-commande-delete-dialog.html',
                    controller: 'TypeCommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeCommande', function(TypeCommande) {
                            return TypeCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-commande', null, { reload: 'type-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
