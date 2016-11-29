(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('detail-commande', {
            parent: 'entity',
            url: '/detail-commande?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.detailCommande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detail-commande/detail-commandes.html',
                    controller: 'DetailCommandeController',
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
                    $translatePartialLoader.addPart('detailCommande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('detail-commande-detail', {
            parent: 'entity',
            url: '/detail-commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.detailCommande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detail-commande/detail-commande-detail.html',
                    controller: 'DetailCommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('detailCommande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DetailCommande', function($stateParams, DetailCommande) {
                    return DetailCommande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'detail-commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('detail-commande-detail.edit', {
            parent: 'detail-commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-commande/detail-commande-dialog.html',
                    controller: 'DetailCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetailCommande', function(DetailCommande) {
                            return DetailCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detail-commande.new', {
            parent: 'detail-commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-commande/detail-commande-dialog.html',
                    controller: 'DetailCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantite: null,
                                localisation: null,
                                ordreCommande: null,
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('detail-commande', null, { reload: 'detail-commande' });
                }, function() {
                    $state.go('detail-commande');
                });
            }]
        })
        .state('detail-commande.edit', {
            parent: 'detail-commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-commande/detail-commande-dialog.html',
                    controller: 'DetailCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetailCommande', function(DetailCommande) {
                            return DetailCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detail-commande', null, { reload: 'detail-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detail-commande.delete', {
            parent: 'detail-commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-commande/detail-commande-delete-dialog.html',
                    controller: 'DetailCommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DetailCommande', function(DetailCommande) {
                            return DetailCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('detail-commande', null, { reload: 'detail-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
